import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import boggle.*;
import boggle.Dictionary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoggleTests {

    //BoggleGame  Test
    @Test
    void findAllWords_small() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BoggleGame game = new BoggleGame();
        Method method = game.getClass().getDeclaredMethod("findAllWords", Map.class, Dictionary.class, BoggleGrid.class);
        method.setAccessible(true);
        // CHANGE "wordlist.txt"
        Dictionary boggleDict = new Dictionary("wordlist.txt");
        Map<String, ArrayList<Position>> allWords = new HashMap<>();
        BoggleGrid grid = new BoggleGrid(4);
        grid.initalizeBoard("RHLDNHTGIPHSNMJO");
        Object r = method.invoke(game, allWords, boggleDict, grid);

        Set<String> expected = new HashSet<>(Arrays.asList("GHOST", "HOST", "THIN"));
        assertEquals(expected, allWords.keySet());
    }

    @Test
    void findAllWords_big() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BoggleGame game = new BoggleGame();
        Method method = game.getClass().getDeclaredMethod("findAllWords", Map.class, Dictionary.class, BoggleGrid.class);
        method.setAccessible(true);

        Dictionary boggleDict = new Dictionary("/Users/laibakhan/csc207/khanlai6/Assignment1/wordlist.txt");
        Map<String, ArrayList<Position>> allWords = new HashMap<>();
        BoggleGrid grid = new BoggleGrid(5);
        grid.initalizeBoard("PIALTPFAETORSSXOMHHVESIAA");
        Object r = method.invoke(game, allWords, boggleDict, grid);

        Set<String> expected = new HashSet<>(Arrays.asList("ROOM", "SEARS", "ALAS", "PROOFS", "SASH", "ALARM", "LEAST", "TEXTS", "ALES", "POOR", "AFAR", "ARMS", "TEXT", "FROM", "MOORS", "ROES", "SALTS", "PROSE", "HAHA", "MORASS", "MOROSE", "FARM", "STEAL", "SHAH", "LETS", "MOOSE", "MORSE", "MOOR", "SEAS", "MOOS", "LASE", "FARMS", "SHAHS", "SEAR", "PROMISE", "LASH", "PROOF", "AROSE", "MORSEL", "ROME", "LAST", "TEST", "LASS", "LEST", "MORALES", "LESS", "AFRO", "PROS", "AFROS", "TEAS", "TEAR", "SEMI", "MESH", "TEAROOM", "AIMS", "SOME", "ROOFS", "SEAL", "SALES", "EAST", "SALE", "RASH", "FORM", "ROSE", "ASSET", "ORAL", "TEAROOMS", "MORALE", "FORMS", "ROOMS", "SALT", "TEARS", "LEASH", "ROOF", "ISMS", "SETTS", "ALTS", "STET", "SETS", "MORAL", "ALARMS", "TEAL", "FAST", "LEAF", "EARS"));
        assertEquals(expected, allWords.keySet());
    }

    //Dictionary Test
    @Test
    void containsWord() {
        Dictionary dict = new Dictionary("/Users/laibakhan/csc207/khanlai6/Assignment1/wordlist.txt");
        assertTrue(dict.containsWord("ENZYME"));
        assertFalse(dict.containsWord("xsnkfn"));
        assertFalse(dict.containsWord("z"));
        assertTrue(dict.containsWord("ZOO"));
        assertTrue(dict.containsWord("zoo"));

    }

    @Test
    void isitPrefix() {
        Dictionary dict = new Dictionary("/Users/laibakhan/csc207/khanlai6/Assignment1/wordlist.txt");
        assertTrue(dict.isPrefix("pench"));
        assertTrue(dict.isPrefix("PENCH"));
        assertFalse(dict.isPrefix("ziem"));
        assertTrue(dict.isPrefix("p")); // Check if this counts as a prefix
        assertTrue(dict.isPrefix(""));

    }



    //BoggleGrid Test
    @Test
    void setupBoard() {
        BoggleGrid grid = new BoggleGrid(10);
        String letters = "";
        for (int i = 0; i < 10; i++) {
            letters = letters + "0123456789";
        }

        grid.initalizeBoard(letters);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(letters.charAt(i*10+j), grid.getCharAt(i, j));
            }
        }
    }

    //BoggleStats Test
    @Test
    void endRoundTest_rounds() {
        BoggleStats stats = new BoggleStats();
        stats.endRound();
        stats.endRound();
        stats.endRound();
        assertEquals(3, stats.getRound());
    }

    @Test
    void endRoundTest_round_score() {
        BoggleStats stats = new BoggleStats();
        stats.addWord("hello", BoggleStats.Player.Human);
        stats.endRound();
        Set<String> set = new HashSet<>();
        assertEquals(1, stats.getRound());
        assertEquals(set, stats.getPlayerWords());
        assertEquals(0, stats.getScore());
        assertEquals(0, stats.getCScore());
    }

    @Test
    void endRoundTest_averages() {
        BoggleStats stats = new BoggleStats();
        stats.addWord("hello", BoggleStats.Player.Human);
        stats.addWord("byee", BoggleStats.Player.Human);
        stats.endRound();
        stats.endRound();
        double num = 2 / 2;
        assertEquals(num, stats.getAvgScore_p());
    }

    @Test
    void endRoundTest_score() {
        BoggleStats stats = new BoggleStats();
        stats.addWord("hello", BoggleStats.Player.Computer);
        stats.addWord("byee", BoggleStats.Player.Computer);
        stats.endRound();
        stats.addWord("byee", BoggleStats.Player.Computer);
        stats.endRound();
        assertEquals(4, stats.getTotScore_c());
    }

    @Test
    void addwords_hum() {
        BoggleStats stats = new BoggleStats();
        stats.addWord("hello", BoggleStats.Player.Human);
        Set<String> set = new HashSet<>();
        set.add("hello");
        assertEquals(set, stats.getPlayerWords());
        assertEquals(2, stats.getScore());
    }

    @Test
    void addMultiplewords_hum() {
        BoggleStats stats = new BoggleStats();
        stats.addWord("hello", BoggleStats.Player.Human);
        stats.addWord("byee", BoggleStats.Player.Human);
        Set<String> set = new HashSet<>();
        set.add("hello");
        set.add("byee");
        assertEquals(set, stats.getPlayerWords());
        assertEquals(3, stats.getScore());
    }

    @Test
    void addwords_comp() {
        BoggleStats stats = new BoggleStats();
        stats.addWord("hello", BoggleStats.Player.Computer);
        Set<String> set = new HashSet<>();
        set.add("hello");
        assertEquals(set, stats.getComputerWords());
        assertEquals(2, stats.getCScore());
    }

    @Test
    void addMultiplewords_comp() {
        BoggleStats stats = new BoggleStats();
        stats.addWord("hello", BoggleStats.Player.Computer);
        stats.addWord("byee", BoggleStats.Player.Computer);
        Set<String> set = new HashSet<>();
        set.add("hello");
        set.add("byee");
        assertEquals(set, stats.getComputerWords());
        assertEquals(3, stats.getCScore());
    }


}
