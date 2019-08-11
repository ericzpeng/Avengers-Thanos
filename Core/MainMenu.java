package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class MainMenu {
    private int width;
    private int height;
    private String seed;
    private final String gameTitle = "CS61B: ENDGAME";
    private HashSet<Character> options;
    private HashSet<Character> allowedSeedValues;
    private Character key;

    public MainMenu(int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;

        options = new HashSet<>();
        options.add('N');
        options.add('n');
        options.add('L');
        options.add('l');
        options.add('Q');
        options.add('q');
        options.add('R');
        options.add('r');


        allowedSeedValues = new HashSet<>();
        allowedSeedValues.add('0');
        allowedSeedValues.add('1');
        allowedSeedValues.add('2');
        allowedSeedValues.add('3');
        allowedSeedValues.add('4');
        allowedSeedValues.add('5');
        allowedSeedValues.add('6');
        allowedSeedValues.add('7');
        allowedSeedValues.add('8');
        allowedSeedValues.add('9');
        allowedSeedValues.add('S');


        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);

        drawText();
        key = solicitInput();

        keyHandler(key);

        StdDraw.enableDoubleBuffering();


    }

    public Character getKey() {
        return key;
    }

    public String getSeed() {
        return seed;
    }


    public void keyHandler(Character c) {
        if (c.equals('N') || c.equals('n')) {
            System.out.println("New Game");
            this.seed = seedText();
            System.out.println(seed);
        } else if (c.equals('L') || c.equals('l') || c.equals('R') || c.equals('r')) {
            System.out.println("Load/Replaying Game");

            try {
                this.seed = readSaveFile();
            } catch (IOException e) {
                System.out.println("IDK");
            }


        } else if (c.equals('Q') || c.equals('q')) {
            System.out.println("Quit");
            System.exit(0);
        }

    }

    static String readSaveFile() throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new FileReader("save.txt"))) {
            return br.readLine();

        }
    }

    private String seedText() {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(width * 1.0 / 2, height * 1.0 / 2, "Enter a seed: ");

        return solicitSeed();


    }



    public void drawSeedFrame(String s) {
        StdDraw.clear(Color.black);
        StdDraw.text(width * 1.0 / 2, height * 1.0 / 2, "Enter a seed: ");
        StdDraw.text(width * 1.0 / 2, height * 1.0 / 3, s);
        StdDraw.show();
    }


    private String solicitSeed() {
        String s = "N";
        drawSeedFrame(s);
        Character lastKeyPressed = '0';
        while (!lastKeyPressed.equals('S') && !lastKeyPressed.equals('s')) {
            if (StdDraw.hasNextKeyTyped()) {
                lastKeyPressed = StdDraw.nextKeyTyped();
                if (allowedSeedValues.contains(lastKeyPressed)) {
                    s = s + lastKeyPressed;
                    drawSeedFrame(s);
                }
            }
        }
        return s;

    }



    private Character solicitInput() {
        Character keyPressed = null;
        while (!options.contains(keyPressed)) {
            if (StdDraw.hasNextKeyTyped()) {
                keyPressed = StdDraw.nextKeyTyped();
            }
        }
        return keyPressed;
    }

    private void drawText() {
        StdDraw.text(width * 1.0 / 2, height - 10, gameTitle);
        StdDraw.text(width * 1.0 / 2, height - 16, "New Game (N)");
        StdDraw.text(width * 1.0 / 2, height - 18, "Load Game (L)");
        StdDraw.text(width * 1.0 / 2, height - 20, "Quit (Q)");
        StdDraw.text(width * 1.0 / 2, height - 22, "Replay (R)");
        //StdDraw.text(width * 1.0 / 2, height - 24, "Enter Name (U)");


    }




}
