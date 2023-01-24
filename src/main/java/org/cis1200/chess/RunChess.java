package org.cis1200.chess;

import java.awt.*;
import javax.swing.*;

public class RunChess implements Runnable {
    @Override
    public void run() {
        final JFrame frame = new JFrame("Chess");
        frame.setLocation(300, 300);
        final JFrame instructionFrame = new JFrame("Chess Instructions");
        final JPanel instructionPanel = new JPanel();
        final JPanel AIInstructionPanel = new JPanel();
        final JPanel headerPanel = new JPanel();
        final JLabel header = new JLabel("Chess!");



        final JLabel AIInstructions = new JLabel(
                "Your opponent is Camel (Chess AI using Minimax EvaLuation), " +
                        "an AI. He looks 3 moves ahead. "
                        +
                        "Camel will tell you about the current game on the bottom of the screen"
        );
        final JLabel instructions = new JLabel(
                "Welcome to Chess. Move a piece by clicking on it, " +
                        "then dragging to the desired position. "
        );
        AIInstructionPanel.add(AIInstructions);
        headerPanel.add(header);
        instructionPanel.add(instructions);
        instructionFrame.add(headerPanel, BorderLayout.NORTH);
        instructionFrame.add(instructionPanel, BorderLayout.CENTER);
        instructionFrame.add(AIInstructionPanel, BorderLayout.SOUTH);
        instructionFrame.pack();
        instructionFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        instructionFrame.setVisible(true);
        instructionFrame.setResizable(false);
        final JPanel AIDialoguePanel = new JPanel();
        frame.add(AIDialoguePanel, BorderLayout.SOUTH);
        final JLabel AIDialogue = new JLabel("You start. Good luck!");
        AIDialoguePanel.add(AIDialogue);


        final JFrame camelFrame = new JFrame("CAMEL v1.0.1");
        final JPanel camelHeaderPanel = new JPanel();
        final JLabel camelHeader = new JLabel("Chess Algorithm using Minimax EvaLuation v1.0.1");
        camelHeaderPanel.add(camelHeader);
        final JPanel camelInfoPanel = new JPanel();
        final JPanel camelCandidatePanel = new JPanel();
        final JPanel camelReportPanel = new JPanel();
        final JPanel camelEvalPanel = new JPanel();
        final JLabel camelStatus = new JLabel("Initializing...");
        final JLabel camelReport = new JLabel("N/A");
        final JLabel camelCandidates = new JLabel("N/A");
        final JLabel camelEvalLabel = new JLabel("0");
        camelInfoPanel.add(camelStatus);
        camelReportPanel.add(camelReport);
        camelCandidatePanel.add(camelCandidates);
        camelEvalPanel.add(camelEvalLabel);
        camelFrame.add(camelHeaderPanel, BorderLayout.BEFORE_FIRST_LINE);
        camelFrame.add(camelEvalPanel, BorderLayout.EAST);
        camelFrame.add(camelCandidatePanel, BorderLayout.CENTER);
        camelFrame.add(camelInfoPanel, BorderLayout.WEST);
        camelFrame.add(camelReportPanel, BorderLayout.AFTER_LAST_LINE);
        camelFrame.setSize(425,250);
        camelFrame.setVisible(true);
        camelFrame.setResizable(false);
        camelFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);



        final Board board = new Board(AIDialogue, true, camelStatus, camelReport, camelCandidates, camelEvalLabel);
        frame.add(board, BorderLayout.CENTER);

        final JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.WEST);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        controlPanel.add(reset);






        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        board.reset();
    }
}
