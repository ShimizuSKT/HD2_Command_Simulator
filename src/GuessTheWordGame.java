import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;

public class GuessTheWordGame extends JFrame {
    private JLabel imageLabel;
    private JTextField guessField;
    private JButton submitButton;
    private JLabel resultLabel;

    private Map<String, String> imageAnswers = new HashMap<>();
    private String currentImagePath; // 保存当前显示图片的路径

    public GuessTheWordGame() {
        super("Guess The Word Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        // 图片显示区域
        imageLabel = new JLabel();
        add(imageLabel, BorderLayout.CENTER);

        // 输入框和提交按钮
        guessField = new JTextField(20);
        submitButton = new JButton("Submit");
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Guess the Word: "));
        inputPanel.add(guessField);
        inputPanel.add(submitButton);
        add(inputPanel, BorderLayout.NORTH);

        // 结果显示区域
        resultLabel = new JLabel();
        add(resultLabel, BorderLayout.SOUTH);

        // 加载图片并保存答案
        loadImagesFromDirectory("C:\\Users\\w0003\\Desktop\\pic"); // 替换为你的图片目录

        // 按钮点击事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });
    }

    private void loadImagesFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            System.err.println("Invalid directory path.");
            return;
        }

        File[] imageFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));
        if (imageFiles == null || imageFiles.length == 0) {
            System.err.println("No image files found in the directory.");
            return;
        }

        Random random = new Random();
        for (File file : imageFiles) {
            try {
                BufferedImage image = ImageIO.read(file);
                if (image != null) {
                    String fileName = file.getName();
                    String answer = fileName.substring(0, fileName.lastIndexOf('.')); // 文件名作为答案
                    imageAnswers.put(file.getAbsolutePath(), answer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (imageAnswers.isEmpty()) {
            System.err.println("No valid image files found in the directory.");
        } else {
            // 随机选择一张图片显示
            currentImagePath = (String) imageAnswers.keySet().toArray()[random.nextInt(imageAnswers.size())];
            displayImage(currentImagePath, 300, 200);
        }
    }

    private void displayImage(String imagePath, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            if (image != null) {
                // 缩放图片
                Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);
                imageLabel.setIcon(icon);
            } else {
                imageLabel.setText("Image Not Found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            imageLabel.setText("Error Loading Image");
        }
    }

    private void checkAnswer() {
        String userGuess = guessField.getText().trim();
        String correctAnswer = imageAnswers.get(currentImagePath);

        if (correctAnswer != null && userGuess.equalsIgnoreCase(correctAnswer)) {
            resultLabel.setText("Congratulations! You guessed it right!");
        } else {
            resultLabel.setText("Oops! Try again...");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GuessTheWordGame game = new GuessTheWordGame();
                game.setVisible(true);
            }
        });
    }
}
