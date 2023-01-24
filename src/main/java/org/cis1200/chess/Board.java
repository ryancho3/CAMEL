package org.cis1200.chess;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel {

    private Chess c;
    private JLabel aiDialogue;
    private boolean processing;
    private Point selected;
    private boolean ai;

    private Move lastMove;

    private final Camel camel;
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    public Board(JLabel aiDialogue, boolean ai, JLabel status, JLabel report, JLabel candidates, JLabel evalLabel) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);
        this.ai = ai;
        this.c = new Chess();
        this.camel = new Camel(c, status, report, candidates, evalLabel);
        this.aiDialogue = aiDialogue;
        this.processing = false;
        this.lastMove = null;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!processing) {
                    selected = e.getPoint();
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (!processing) {
                    Point p = e.getPoint();
                    Move m = new Move(
                            new int[] { selected.y / 100, selected.x / 100 },
                            new int[] { p.y / 100, p.x / 100 }
                    );
                    if (c.makeMove(m)) {
                        lastMove = m;
                    }
                    updateAIDialogue();
                    repaint();
                    if (ai) {
                        SwingUtilities.invokeLater(() -> {
                            aiMove();
                            camel.updateGameState(c);
                        });
                    }
                }

            }
        });
    }

    public void reset() {
        c.reset();
        updateAIDialogue();
        camel.updateGameState(c);
        repaint();
        requestFocusInWindow();
        this.lastMove = null;
        this.processing = false;
    }

    public void aiMove() {
        this.processing = true;
        camel.updateGameState(this.c);
        Move m = camel.selectMove();
        if (this.c.makeMove(m)) {
            this.lastMove = m;
        }
        updateAIDialogue();
        repaint();
        this.processing = false;
    }

    public void updateAIDialogue() {
        if (c.getCurrentPlayer()) {
            aiDialogue.setText("Your turn!");
            if (c.checkCheck()) {
                aiDialogue.setText("Check!");
            }
            if (c.checkStalemate()) {
                aiDialogue.setText("Stalemate...");
            }
            if (c.checkCheckmate()) {
                aiDialogue.setText("Checkmate, I win!");
            }
        } else {
            aiDialogue.setText("My turn. Thinking...");
            if (c.checkCheck()) {
                aiDialogue.setText("Uh oh. I'm checked! Thinking...");
            }
            if (c.checkStalemate()) {
                aiDialogue.setText("Stalemate...");
            }
            if (c.checkCheckmate()) {
                aiDialogue.setText("Checkmate, You win!");
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color color = Color.WHITE;
        g.setColor(color);
        for (int i = 0; i < 8; i++) {
            if (!color.equals(Color.WHITE)) {
                color = Color.WHITE;
            } else {
                color = Color.DARK_GRAY;
            }
            g.setColor(color);
            for (int j = 0; j < 8; j++) {
                if (!color.equals(Color.WHITE)) {
                    color = Color.WHITE;
                } else {
                    color = Color.DARK_GRAY;
                }
                g.setColor(color);
                if (lastMove != null && ((j==lastMove.getStart()[0] && i==lastMove.getStart()[1]) || (j==lastMove.getEnd()[0] && i==lastMove.getEnd()[1]))) {
                    g.setColor(Color.CYAN);
                }
                g.fillRect(i * 100, j * 100, 100, 100);
                Piece p = this.c.get(j, i);
                if (p != null) {
                    p.draw(g, i * 100, j * 100);
                }
                g.drawLine(0, j*100, 800, j*100);
            }
            g.drawLine(i*100, 0, i*100, 800);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
