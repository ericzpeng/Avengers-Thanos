package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    private boolean load = false;
    private boolean replay = false;



    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        MainMenu menu = new MainMenu(40, 40);
        //System.out.println(menu.getSeed());

        if (menu.getKey().equals('L') || menu.getKey().equals('l')) {
            load = true;
            interactWithInputString(menu.getSeed());
        } else if (menu.getKey().equals('N') || menu.getKey().equals('n')) {
            String seedString = menu.getSeed().replaceAll("[^0-9]", "");
            long seed = Long.parseLong(seedString);
            CreateMap map = new CreateMap(ter, seed, new KeyboardInputSource(), load, replay);
        } else if (menu.getKey().equals('R') || menu.getKey().equals('r')) {
            load = false;
            replay = true;
            interactWithInputString(menu.getSeed());

        }

    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        // @Source looked up how to change string to long
        // https://beginnersbook.com/2013/12/how-to-convert-string-to-long-in-java/

        if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
            try {
                String saved = readSaveFile();
                input = saved + input.substring(1);

                System.out.println(input);


            } catch (IOException e) {
                System.out.println("IDK");
            }

        }


        String seedString = input.replaceAll("[^0-9]", "");

        long seed = Long.parseLong(seedString); //seed number

        String inputString = input.replaceAll("[0-9]", "");
        inputString = inputString.replaceAll("N", "");
        //inputString = inputString.replaceAll("S", "");
        inputString = inputString.toUpperCase();

        InputSource inputSource = new StringInputDevice(inputString);



        CreateMap map = new CreateMap(ter, seed, inputSource, load, replay);

        TETile[][] finalWorldFrame = map.getWorld();


        return finalWorldFrame;
    }


    private static String readSaveFile() throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new FileReader("save.txt"))) {
            return br.readLine();

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }

        return "N123";

    }


}
