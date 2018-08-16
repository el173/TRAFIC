import jxl.write.WriteException;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;

import static org.opencv.imgproc.Imgproc.resize;

public class GUI {
    private JLabel imageView;
    private JFrame frame;
    private JFrame frameBGS;
    private JLabel BGSview;

    private JButton playPauseButton;
    JButton loadButton;
    private JButton resetButton;
    private JButton countingLineButton;
    private JButton speedLineButton;


    private volatile boolean isPaused = true;
    private boolean crossingLine = false;
    private boolean crossingSpeedLine = false;
    private int areaThreshold = 1700;
    private double imageThreshold = 20;
    private int history = 1500;
    private int vehicleSizeThreshold = 20000;

    private VideoCapture capture;
    private Mat currentImage = new Mat();
    private VideoProcessor videoProcessor = new MixtureOfGaussianBackground(imageThreshold, history);
    private ImageProcessor imageProcessor = new ImageProcessor();
    private Mat foregroundImage;

    private Point lineCount1;           //new Point(370,200);
    private volatile Point lineCount2;          //new Point(400,280);
    private Point lineSpeed1;           //new Point(460,200);
    private volatile Point lineSpeed2;          //new Point(490,270);
    private int counter = 0;
    private int lastTSM = 0;
    private HashMap<Integer, Integer> speed = new HashMap<Integer, Integer>();

    private double distanceCS = 6.0;
    private double videoFPS;
    private int maxFPS;
    private int whichFrame;
    private JSpinner distanceBLfield;

    private volatile String videoPath;

    private JFormattedTextField carsAmountField;
    private JFormattedTextField carsSpeedField;
    private JFormattedTextField vansAmountField;
    private JFormattedTextField vansSpeedField;
    private JFormattedTextField lorriesAmountField;
    private JFormattedTextField lorriesSpeedField;

    private int numberOfVehicles = 0;
    private double sumSpeedVehicle = 0;
    private int divisorVehicle = 1;

    private boolean mouseListenertIsActive;
    private boolean mouseListenertIsActive2;
    private boolean startDraw;
    private Mat copiedImage;

    private volatile boolean loopBreaker = false;

    private JButton BGSButton;
    private JSpinner imgThresholdField;
    private volatile boolean isBGSview = false;
    private Mat ImageBGS = new Mat();

    private JSpinner videoHistoryField;

    private JFormattedTextField currentTimeField;
    private double timeInSec;
    private int minutes = 1;
    private int second = 0;

    private JButton realTimeButton;
    private volatile boolean isProcessInRealTime = true;
    private long startTime;
    private long oneFrameDuration;

    private Mat foregroundClone;

    public void init() throws IOException, WriteException, InterruptedException {
        setSystemLookAndFeel();
        initGUI();

        while (true) {
            if (videoPath != null) {
                countingLineButton.setEnabled(true);
                speedLineButton.setEnabled(true);
                distanceBLfield.setEnabled(true);

                resetButton.setEnabled(true);
                break;
            }
        }

        while (true) {
            if (lineSpeed2 != null && lineCount2 != null) {
                playPauseButton.setEnabled(true);
                break;
            }
        }


        Thread mainLoop = new Thread(new Loop());
        mainLoop.start();
    }

    public void initGUI() {
        frame = createJFrame("TRAFIC speedDetector");

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        playPauseButton.setEnabled(false);
        countingLineButton.setEnabled(false);
        speedLineButton.setEnabled(false);
        distanceBLfield.setEnabled(false);
        resetButton.setEnabled(false);

    }

    public class Loop implements Runnable {

        @Override
        public void run() {

            maxWaitingFPS();
            videoProcessor = new MixtureOfGaussianBackground(imageThreshold, history);
            if (capture.isOpened()) {
                while (true) {
                    if (!isPaused) {
                        capture.read(currentImage);
                        if (!currentImage.empty()) {
                            resize(currentImage, currentImage, new Size(640, 360));
                            foregroundImage = currentImage.clone();
                            foregroundImage = videoProcessor.process(foregroundImage);

                            foregroundClone = foregroundImage.clone();
                            Imgproc.bilateralFilter(foregroundClone, foregroundImage, 2, 1600, 400);

                            if (isBGSview) {
                                resize(foregroundImage, ImageBGS, new Size(430, 240));
                                BGSview.setIcon(new ImageIcon(imageProcessor.toBufferedImage(ImageBGS)));
                            }

                            CountVehicles countVehicles = new CountVehicles(areaThreshold, vehicleSizeThreshold, lineCount1, lineCount2, lineSpeed1, lineSpeed2, crossingLine, crossingSpeedLine);
                            countVehicles.findAndDrawContours(currentImage, foregroundImage);

                            try {
                                count(countVehicles);
                                speedMeasure(countVehicles);
                            } catch (WriteException e) {
                                e.printStackTrace();
                            }


                            if (isProcessInRealTime) {
                                long time = System.currentTimeMillis() - startTime;
                                if (time < oneFrameDuration) {
                                    try {
                                        Thread.sleep(oneFrameDuration - time);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            updateView(currentImage);
                            startTime = System.currentTimeMillis();

                            if (loopBreaker)
                                break;

                        } else {

                            playPauseButton.setEnabled(false);
                            loadButton.setEnabled(true);

                            playPauseButton.setText("Play");
                            minutes = 1;
                            second = 0;
                            whichFrame = 0;
                            System.out.println("The video has finished!");
                            break;
                        }
                    }
                }
            }
        }
    }


    public synchronized void count(CountVehicles countVehicles) throws WriteException {
        if (countVehicles.isVehicleToAdd()) {
            counter++;
            lastTSM++;
            speed.put(lastTSM, 0);
            numberOfVehicles++;
            lorriesAmountField.setValue(numberOfVehicles);
        }
        crossingLine = countVehicles.isCrossingLine();
    }

    public synchronized void speedMeasure(CountVehicles countVehicles) throws WriteException {
        if (!speed.isEmpty()) {
            int firstTSM = speed.entrySet().iterator().next().getKey();
            if (countVehicles.isToSpeedMeasure()) {
                for (int i = firstTSM; i <= lastTSM; i++) {
                    if (speed.containsKey(i)) {
                        speed.put(i, (speed.get(i) + 1));
                    }
                }

                double currentSpeed = computeSpeed(speed.get(firstTSM));

                sumSpeedVehicle = sumSpeedVehicle + currentSpeed;
                double avgspeed3 = sumSpeedVehicle / divisorVehicle;
                divisorVehicle++;
                lorriesSpeedField.setValue(avgspeed3);


                speed.remove(firstTSM);

            } else {
                for (int i = firstTSM; i <= lastTSM; i++) {
                    if (speed.containsKey(i)) {
                        int currentFPS = speed.get(i);
                        speed.put(i, (currentFPS + 1));
                        if (currentFPS > maxFPS) {
                            speed.remove(i);
                            numberOfVehicles--;
                            lorriesAmountField.setValue(numberOfVehicles);
                        }
                    }
                }
            }
        }
        crossingSpeedLine = countVehicles.isCrossingSpeedLine();
    }

    private JFrame createJFrame(String windowName) {
        frame = new JFrame(windowName);
        frame.setLayout(new GridBagLayout());

        setupVideo(frame);

        reset(frame);
        playPause(frame);

        loadFile(frame);

        infoCars(frame);
        infoVans(frame);
        infoLorries(frame);

        selectCountingLine(frame);
        selectSpeedLine(frame);
        setupDistanceBetweenLines(frame);

        setupImageThreshold(frame);
        setupVideoHistory(frame);
        setupAreaThreshold(frame);
        setupVehicleSizeThreshold(frame);

        setupBGSvisibility(frame);
        currentTime(frame);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    private void setupVideo(JFrame frame) {
        imageView = new JLabel();


        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 6;
        c.gridheight = 9;

        frame.add(imageView, c);

        Mat localImage = new Mat(new Size(640, 360), CvType.CV_8UC3, new Scalar(255, 255, 255));
        resize(localImage, localImage, new Size(640, 360));
        updateView(localImage);
    }

    private void playPause(JFrame frame) {

        playPauseButton = new JButton("Play");
        playPauseButton.setPreferredSize(new Dimension(100, 40));

        playPauseButton.addActionListener(event -> {
            if (!isPaused) {
                isPaused = true;
                playPauseButton.setText("Play");

                loadButton.setEnabled(true);

                countingLineButton.setEnabled(true);
                distanceBLfield.setEnabled(true);
                speedLineButton.setEnabled(true);

            } else {
                isPaused = false;
                playPauseButton.setText("Pause");

                maxWaitingFPS();

                loadButton.setEnabled(false);


                countingLineButton.setEnabled(false);
                distanceBLfield.setEnabled(false);
                speedLineButton.setEnabled(false);

                frame.pack();
            }
        });
        playPauseButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 10, 15, 10);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;

        frame.add(playPauseButton, c);
    }

    private void loadFile(JFrame frame) {

        JTextField field = new JTextField();
        field.setText(" ");
        field.setEditable(false);

        loadButton = new JButton("Open a video", createImageIcon("resources/Open16.gif"));

        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Video Files", "avi", "mp4", "mpg", "mov");
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(new File(System.getProperty("user.home"), "Desktop"));
        fc.setAcceptAllFileFilterUsed(false);

        loadButton.addActionListener(event -> {
            int returnVal = fc.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                videoPath = file.getPath();
                field.setText(videoPath);
                capture = new VideoCapture(videoPath);
                capture.read(currentImage);
                videoFPS = capture.get(Videoio.CAP_PROP_FPS);
                resize(currentImage, currentImage, new Size(640, 360));
                updateView(currentImage);

            }
        });
        loadButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 5, 0, 5);
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        frame.add(loadButton, c);

        c.insets = new Insets(0, 0, 0, 10);
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 3;
        frame.add(field, c);
    }

    private void infoCars(JFrame frame) {
        JLabel quantityLabel = new JLabel("Quantity", JLabel.RIGHT);
        quantityLabel.setFont(new Font("defaut", Font.BOLD, 12));

        JLabel averageLabel = new JLabel("Average speed [km/h]", JLabel.RIGHT);
        averageLabel.setFont(new Font("defaut", Font.BOLD, 12));

        JLabel carsLabel = new JLabel("Cars", JLabel.CENTER);
        carsLabel.setFont(new Font("defaut", Font.BOLD, 12));

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        carsAmountField = new JFormattedTextField(numberFormat);
        carsAmountField.setValue(new Integer(0));
        carsAmountField.setBackground(Color.YELLOW);
        carsAmountField.setEditable(false);
        carsAmountField.setPreferredSize(new Dimension(50, 20));
        carsAmountField.setHorizontalAlignment(JFormattedTextField.CENTER);

        carsSpeedField = new JFormattedTextField(numberFormat);
        carsSpeedField.setValue(new Integer(0));
        carsSpeedField.setBackground(Color.GREEN);
        carsSpeedField.setEditable(false);
        carsSpeedField.setPreferredSize(new Dimension(50, 20));
        carsSpeedField.setHorizontalAlignment(JFormattedTextField.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 12;
        c.gridwidth = 1;
        c.insets = new Insets(0, 70, 5, 5);
        frame.add(quantityLabel, c);

        c.gridy = 13;
        frame.add(averageLabel, c);

        c.insets = new Insets(0, 0, 5, 5);
        c.gridx = 4;
        c.gridy = 11;
        frame.add(carsLabel, c);

        c.gridy = 12;
        frame.add(carsAmountField, c);

        c.gridy = 13;
        frame.add(carsSpeedField, c);
    }

    private void infoVans(JFrame frame) {

        JLabel vansLabel = new JLabel("Vans", JLabel.CENTER);
        vansLabel.setFont(new Font("defaut", Font.BOLD, 12));

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        vansAmountField = new JFormattedTextField(numberFormat);
        vansAmountField.setValue(new Integer(0));
        vansAmountField.setBackground(Color.YELLOW);
        vansAmountField.setEditable(false);
        vansAmountField.setPreferredSize(new Dimension(50, 20));
        vansAmountField.setHorizontalAlignment(JFormattedTextField.CENTER);

        vansSpeedField = new JFormattedTextField(numberFormat);
        vansSpeedField.setValue(new Integer(0));
        vansSpeedField.setBackground(Color.GREEN);
        vansSpeedField.setEditable(false);
        vansSpeedField.setPreferredSize(new Dimension(50, 20));
        vansSpeedField.setHorizontalAlignment(JFormattedTextField.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 11;
        c.gridwidth = 1;
        c.insets = new Insets(0, 5, 5, 5);
        frame.add(vansLabel, c);

        c.gridy = 12;
        frame.add(vansAmountField, c);

        c.gridy = 13;
        frame.add(vansSpeedField, c);
    }

    private void infoLorries(JFrame frame) {

        JLabel lorriesLabel = new JLabel("Lorries", JLabel.CENTER);
        lorriesLabel.setFont(new Font("defaut", Font.BOLD, 12));

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        lorriesAmountField = new JFormattedTextField(numberFormat);
        lorriesAmountField.setValue(new Integer(0));
        lorriesAmountField.setBackground(Color.YELLOW);
        lorriesAmountField.setEditable(false);
        lorriesAmountField.setPreferredSize(new Dimension(50, 20));
        lorriesAmountField.setHorizontalAlignment(JFormattedTextField.CENTER);

        lorriesSpeedField = new JFormattedTextField(numberFormat);
        lorriesSpeedField.setValue(new Integer(0));
        lorriesSpeedField.setBackground(Color.GREEN);
        lorriesSpeedField.setEditable(false);
        lorriesSpeedField.setPreferredSize(new Dimension(50, 20));
        lorriesSpeedField.setHorizontalAlignment(JFormattedTextField.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 6;
        c.gridy = 11;
        c.gridwidth = 1;
        c.insets = new Insets(0, 5, 5, 285);
        frame.add(lorriesLabel, c);

        c.gridy = 12;
        frame.add(lorriesAmountField, c);

        c.gridy = 13;
        frame.add(lorriesSpeedField, c);
    }

    private void updateView(Mat image) {
        imageView.setIcon(new ImageIcon(imageProcessor.toBufferedImage(image)));
    }

    public void maxWaitingFPS() {
        double time = (distanceCS / 3);
        double max = videoFPS * time;
        maxFPS = (int) max;

        oneFrameDuration = 1000 / (long) videoFPS;
    }


    public double computeSpeed(int speedPFS) {
        double duration = speedPFS / videoFPS;
        double v = (distanceCS / duration) * 3.6;
        return v;
    }

    private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = GUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void reset(JFrame frame) {
        resetButton = new JButton("Reset");

        resetButton.addActionListener(event -> {

            int n = JOptionPane.showConfirmDialog(
                    frame, "Are you sure you want to reset the video?",
                    "Reset", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                loopBreaker = true;

                capture = new VideoCapture(videoPath);
                capture.read(currentImage);
                videoFPS = capture.get(Videoio.CAP_PROP_FPS);
                resize(currentImage, currentImage, new Size(640, 360));
                updateView(currentImage);

                currentTimeField.setValue("0 sec");

                isPaused = true;
                playPauseButton.setText("Play");
                playPauseButton.setEnabled(false);
                videoProcessor = new MixtureOfGaussianBackground(imageThreshold, history);

                resetButton.setEnabled(false);

                countingLineButton.setEnabled(true);
                speedLineButton.setEnabled(true);
                distanceBLfield.setEnabled(true);
                lineCount1 = null;
                lineCount2 = null;
                lineSpeed1 = null;
                lineSpeed2 = null;

                minutes = 1;
                second = 0;
                whichFrame = 0;
                timeInSec = 0;

                carsAmountField.setValue(new Integer(0));
                carsSpeedField.setValue(new Integer(0));
                vansAmountField.setValue(new Integer(0));
                vansSpeedField.setValue(new Integer(0));
                lorriesAmountField.setValue(new Integer(0));
                lorriesSpeedField.setValue(new Integer(0));

                numberOfVehicles = 0;
                sumSpeedVehicle = 0;
                divisorVehicle = 1;

                counter = 0;
                lastTSM = 0;

                Thread reseting = new Thread(new Reseting());
                reseting.start();
                loopBreaker = false;
            }

        });

        resetButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 60, 5, 60);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        frame.add(resetButton, c);
    }

    private class Reseting implements Runnable {

        @Override
        public void run() {

            while (true) {
                if (lineSpeed2 != null && lineCount2 != null) {
                    playPauseButton.setEnabled(true);
                    resetButton.setEnabled(true);

                    Thread mainLoop = new Thread(new Loop());
                    mainLoop.start();

                    break;
                }
            }
        }
    }

    private void setupDistanceBetweenLines(JFrame frame) {
        JLabel distanceBLLabel = new JLabel("Distance between lines [m]:", JLabel.RIGHT);
        distanceBLLabel.setFont(new Font("defaut", Font.BOLD, 11));

        distanceBLfield = new JSpinner(new SpinnerNumberModel(distanceCS, 0, 10, 0.5));
        distanceBLfield.setAlignmentX(Component.LEFT_ALIGNMENT);
        distanceBLfield.setPreferredSize(new Dimension(55, 26));

        distanceBLfield.addChangeListener(e ->
                distanceCS = (double) distanceBLfield.getValue());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.insets = new Insets(0, 5, 0, 0);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        frame.add(distanceBLLabel, c);

        c.insets = new Insets(0, 0, 0, 30);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        frame.add(distanceBLfield, c);
    }

    private void selectCountingLine(JFrame frame) {

        countingLineButton = new JButton("Draw a counting line");
        countingLineButton.setPreferredSize(new Dimension(120, 40));

        countingLineButton.addActionListener(event -> {
            countingLineButton.setEnabled(false);
            speedLineButton.setEnabled(false);
            mouseListenertIsActive = true;
            startDraw = false;
            imageView.addMouseListener(ml);
            imageView.addMouseMotionListener(ml2);

        });
        countingLineButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 5, 10, 5);
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;

        frame.add(countingLineButton, c);
    }

    private void selectSpeedLine(JFrame frame) {

        speedLineButton = new JButton("Draw a speed line");
        speedLineButton.setPreferredSize(new Dimension(120, 40));

        speedLineButton.addActionListener(event -> {
            countingLineButton.setEnabled(false);
            speedLineButton.setEnabled(false);
            mouseListenertIsActive2 = true;
            startDraw = false;
            imageView.addMouseListener(ml);
            imageView.addMouseMotionListener(ml2);

        });
        speedLineButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 5);
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 1;

        frame.add(speedLineButton, c);
    }

    public void call(int event, Point point) {
        if (event == 1) {
            if (!startDraw) {
                lineCount1 = point;
                startDraw = true;
            } else {
                lineCount2 = point;
                startDraw = false;
                mouseListenertIsActive = false;
                countingLineButton.setEnabled(true);
                speedLineButton.setEnabled(true);
                imageView.removeMouseListener(ml);
                imageView.removeMouseMotionListener(ml2);
            }

        } else if (event == 0 && startDraw) {
            copiedImage = currentImage.clone();
            Imgproc.line(copiedImage, lineCount1, point, new Scalar(0, 0, 255), 1);
            if (lineSpeed1 != null && lineSpeed2 != null)
                Imgproc.line(copiedImage, lineSpeed1, lineSpeed2, new Scalar(0, 255, 0), 1);
            updateView(copiedImage);
        }
    }

    private void call2(int event, Point point) {
        if (event == 1) {
            if (!startDraw) {
                lineSpeed1 = point;
                startDraw = true;
            } else {
                lineSpeed2 = point;
                startDraw = false;
                mouseListenertIsActive2 = false;
                countingLineButton.setEnabled(true);
                speedLineButton.setEnabled(true);
                imageView.removeMouseListener(ml);
                imageView.removeMouseMotionListener(ml2);
            }

        } else if (event == 0 && startDraw) {
            copiedImage = currentImage.clone();
            Imgproc.line(copiedImage, lineSpeed1, point, new Scalar(0, 255, 0), 1);
            if (lineCount1 != null && lineCount2 != null)
                Imgproc.line(copiedImage, lineCount1, lineCount2, new Scalar(0, 0, 255), 1);
            updateView(copiedImage);
        }
    }

    private MouseListener ml = new MouseListener() {
        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if (mouseListenertIsActive) {
                call(e.getButton(), new Point(e.getX(), e.getY()));
            } else if (mouseListenertIsActive2) {
                call2(e.getButton(), new Point(e.getX(), e.getY()));
            }
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    };

    private MouseMotionListener ml2 = new MouseMotionListener() {
        public void mouseDragged(MouseEvent e) {

        }

        public void mouseMoved(MouseEvent e) {
            if (mouseListenertIsActive) {
                call(e.getButton(), new Point(e.getX(), e.getY()));
            } else if (mouseListenertIsActive2) {
                call2(e.getButton(), new Point(e.getX(), e.getY()));
            }
        }
    };

    private void setupImageThreshold(JFrame frame) {
        JLabel imgThresholdLabel = new JLabel("Video threshold:", JLabel.RIGHT);

        imgThresholdField = new JSpinner(new SpinnerNumberModel(imageThreshold, 0, 10000, 5));
        imgThresholdField.setAlignmentX(Component.LEFT_ALIGNMENT);

        imgThresholdField.addChangeListener(e -> {
            imageThreshold = (double) imgThresholdField.getValue();
            videoProcessor.setImageThreshold(imageThreshold);
        });

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        frame.add(imgThresholdLabel, c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        frame.add(imgThresholdField, c);
    }

    private void setupVideoHistory(JFrame frame) {
        JLabel videoHistoryLabel = new JLabel("History:", JLabel.RIGHT);

        videoHistoryField = new JSpinner(new SpinnerNumberModel(history, 0, 100000, 50));
        videoHistoryField.setAlignmentX(Component.LEFT_ALIGNMENT);

        videoHistoryField.addChangeListener(e -> {
            history = (int) videoHistoryField.getValue();
            videoProcessor.setHistory(history);
        });

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        frame.add(videoHistoryLabel, c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        frame.add(videoHistoryField, c);
    }

    private void setupAreaThreshold(JFrame frame) {
        JLabel areaThresholdLabel = new JLabel("Area threshold:", JLabel.RIGHT);

        final JSpinner areaThresholdField = new JSpinner(new SpinnerNumberModel(areaThreshold, 0, 100000, 50));
        areaThresholdField.setAlignmentX(Component.LEFT_ALIGNMENT);

        areaThresholdField.addChangeListener(e ->
                areaThreshold = (int) areaThresholdField.getValue());

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 1;
        frame.add(areaThresholdLabel, c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        frame.add(areaThresholdField, c);
    }

    private void setupVehicleSizeThreshold(JFrame frame) {
        JLabel vehicleSizeThresholdLabel = new JLabel("Vehicle size threshold:", JLabel.RIGHT);

        final JSpinner vehicleSizeThresholdField = new JSpinner(new SpinnerNumberModel(vehicleSizeThreshold, 0, 100000, 100));
        vehicleSizeThresholdField.setAlignmentX(Component.LEFT_ALIGNMENT);

        vehicleSizeThresholdField.addChangeListener(e ->
                vehicleSizeThreshold = (int) vehicleSizeThresholdField.getValue());

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 1;
        frame.add(vehicleSizeThresholdLabel, c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        frame.add(vehicleSizeThresholdField, c);
    }

    private void initBGSview() {
        frameBGS = new JFrame("BGS View");
        BGSview = new JLabel();
        frameBGS.add(BGSview);
        Mat localImage = new Mat(new Size(430, 240), CvType.CV_8UC3, new Scalar(255, 255, 255));
        BGSview.setIcon(new ImageIcon(imageProcessor.toBufferedImage(localImage)));
        frameBGS.setVisible(true);
        frameBGS.pack();

        frameBGS.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                BGSButton.setEnabled(true);
                isBGSview = false;
            }
        });

    }

    private void setupBGSvisibility(JFrame frame) {
        BGSButton = new JButton("BGS view");
        BGSButton.setPreferredSize(new Dimension(200, 30));

        BGSButton.addActionListener(event -> {
            initBGSview();
            BGSButton.setEnabled(false);
            isBGSview = true;
        });

        GridBagConstraints c = new GridBagConstraints();
        BGSButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 2;

        frame.add(BGSButton, c);

    }

    private void currentTime(JFrame frame) {

        JLabel currentTimeLabel = new JLabel("Real time:", JLabel.RIGHT);
        currentTimeLabel.setFont(new Font("Arial", Font.BOLD, 12));

        currentTimeField = new JFormattedTextField();
        currentTimeField.setValue("0 sec");
        currentTimeField.setHorizontalAlignment(JFormattedTextField.CENTER);
        currentTimeField.setEditable(false);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 13;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 0, 20);
        frame.add(currentTimeLabel, c);
        c.insets = new Insets(0, 0, 0, 40);
        c.gridx = 1;
        frame.add(currentTimeField, c);
    }

    private void setupRealTime(JFrame frame) {

        realTimeButton = new JButton("Process in real time OFF");
        realTimeButton.setPreferredSize(new Dimension(150, 35));
        realTimeButton.addActionListener(event -> {
            if (isProcessInRealTime) {
                isProcessInRealTime = false;
                realTimeButton.setText("Process in real time OFF");
            } else {
                isProcessInRealTime = true;
                realTimeButton.setText("Process in real time ON");
            }

        });
        realTimeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 11;
        c.gridwidth = 2;
        c.gridheight = 2;

        frame.add(realTimeButton, c);
    }

    private void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
