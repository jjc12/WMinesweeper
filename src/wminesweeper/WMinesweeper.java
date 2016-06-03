package wminesweeper;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

class MineSmileTimer extends JPanel{
    
    
    //smile face that when clicked, resets the game.
    private JLabel smile;
    //timer
    final private Timer timer;
    //delay for timer 1000(ms) = 1 sec
    final private int delay = 1000;
    //left counter of number of flags placed
    final private Label mineNumbers;
    //right counter of time, which stops when mine is clicked
    //or reset upon click of smile button.
    private Label timeCounter;
    //what goes in the JLabel timeCounter
    private int timeNumber;
    //counts the flags in the game
    private int flagCounter;
    //counts the number of squares left in the game
    private int SquaresLeft;
    
    public MineSmileTimer(){
        
        //initialize the smile button
        smile = new JLabel();
        //initialize the flags label
        mineNumbers = new Label();
        //initialize the time label
        timeCounter = new Label();
        //set the layout of the three buttons (flags, smile, timer) to BoxLayout
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        //define the smile button (label for now)
        try{
        smile.setIcon(
                new ImageIcon(
                        ImageIO.read(getClass()
                                .getResource("smiley.png"))));
        } catch(IOException ex){
            smile.setText(":)");
        }
    
        //define the label that uses the timer
        timer = new Timer(delay, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            timeCounter.setText(Integer.toString(timeNumber++));
            timeCounter.validate();
        }});
        
        //define the label that counts flags
        mineNumbers.setText(Integer.toString(10 - flagCounter));
        
        //set alignment of labels
        mineNumbers.setAlignment(Label.LEFT);
        smile.setHorizontalAlignment(SwingConstants.CENTER);
        timeCounter.setAlignment(Label.RIGHT);
        
        //add the flag counter
        add(mineNumbers);
        //add the smile button
        add(smile);
        //add the timer label
        add(timeCounter);
    }

    class SquarePanel extends JPanel implements ActionListener {

        /**
         * Private data goes here.
        */
        //buttons for clicking, and hopefully not landing on mines! ;)
        private JButton[] buttons;
        //AbstractButton for downcasting an Object in method actionPerformed
        private AbstractButton c;
        //String to determine whether square is safe (no mine) or not (mine)
        private String safeOrNot;
        //String that gets the key's value in the action listener
        private String key_value;
        //The key as a String. The right-hand value is meaningless, actually
        final private String myKey = Integer.toString(9);
        //Counts the number of mines on the board.
        private int mineCounter;
        //Counts the number of mines around a square.
        private int numberOfMines;
        //Random number generator for mine placement
        private Random rand;
        //gets ActionListeners for use in the clickedRight() method
        private ActionListener[] arr;

        /**
         * Constructor goes here.
         */
        public SquarePanel(){

            //random number generator for random mine placement
            rand = new Random();

            //in the future, we may want the GridLayout to be determined by
            //the parameters passed in the main function (see main)

            //set the layout to a gridlayout.
            setLayout(new GridLayout(9, 9));

            buttons = new JButton[81];
            for(int i = 0; i < 81; ++i){
                buttons[i] = new JButton();
                //add a name that stores the location of the button
                buttons[i].setName(Integer.toString(i));
                //register button to receive events
                buttons[i].addActionListener(this);
                //register button to receive right-click events for mine flags
                buttons[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(SwingUtilities.isRightMouseButton(e)){
                            clickedRight(e);
                        }
                    }
                 });
                //add the button to the JPanel now.
                add(buttons[i]);
            }

            mineCounter = 0;
            SquaresLeft = 81;
            for(int i = 0; i < 81; ++i){
                //determine whether button will reveal a mine or not
                if(rand.nextInt(10) == 0 && mineCounter < 10){
                    safeOrNot = "Mine";
                    //place the mine
                    //skips if there is a mine there already.
                    if(!(((String)buttons[i].getClientProperty(myKey))
                            == null)){
                        continue;
                    }
                    buttons[i].putClientProperty(myKey, safeOrNot);
                    ++mineCounter;
                    //Console output to detect mines
                    //System.out.println("Mine " + mineCounter
                        //+ " located at " + i);
                    if(mineCounter == 10){
                        break;
                    }
                }
                if(i == 80 && mineCounter < 10){
                    i = -1;
                }

            }

            //initialize other clientproperties from null to "0"
            //fixes the "null pointer exception" problem
            for(int i = 0; i < 81; ++i){
                if((String)buttons[i].getClientProperty(myKey) == null){
                    buttons[i].putClientProperty(myKey, Integer.toString(0));
                }
            }

            //count the number of mines around the square
            //if square doesn't hold a mine
            for(int i = 0; i < 81; ++i){
                //place the number
                numberOfMines = 0;
                if(!((String)buttons[i].getClientProperty(myKey))
                        .equals("Mine")){
                    if(8 < i && i < 72 && i % 9 != 0 && (i + 1) % 9 != 0){
                    //8 conditionals, each detecting whether a mine is around or
                    //not.
                        if(((String)buttons[i - 10].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i - 9].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i - 8].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i - 1].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 1].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 8].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 9].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 10].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }                    
                    }
                    //specialized cases:
                    //else if square is a border square.
                    //specifically if square is a corner square.                
                    else if(i == 0){
                        if(((String)buttons[1].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[9].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[10].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }                        
                    }
                    else if(i == 8){
                        if(((String)buttons[7].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[16].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[17].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }                        
                    }
                    else if(i == 72){
                        if(((String)buttons[73].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[63].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[64].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }                        
                    }
                    else if(i == 80){
                        if(((String)buttons[79].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[71].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[70].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }                        
                    }
                    //4 conditionals, each for the 4 borders, not counting
                    //corners. then 5 conditionals inside each.                    
                    else if(0 < i && i < 8){
                        if(((String)buttons[i + 8].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 9].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 10].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 1].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        } 
                        if(((String)buttons[i - 1].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }                        
                    }
                    else if(i % 9 == 0){
                        if(((String)buttons[i - 9].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i - 8].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 1].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 9].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 10].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }                        
                    }
                    else if((i + 1) % 9 == 0){
                        if(((String)buttons[i - 10].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i - 9].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i - 1].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 8].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i + 9].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }                        
                    }
                    else if(72 < i && i < 80){
                        if(((String)buttons[i - 8].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i - 9].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }
                        if(((String)buttons[i - 10].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }  
                        if(((String)buttons[i + 1].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        } 
                        if(((String)buttons[i - 1].getClientProperty(myKey))
                                .equals("Mine")){
                            ++numberOfMines;
                        }                        
                    }
                safeOrNot = Integer.toString(numberOfMines);
                buttons[i].putClientProperty(myKey, safeOrNot);                
                }
            }
        }

        /**
         * Override actionPerformed (necessary for interface ActionListener).
         */
        @Override
        public void actionPerformed(ActionEvent event){

            //1. Which button was clicked? Downcast the Object to a button
            //since EventObject.getSource(); returns the Object
            c = (AbstractButton)event.getSource();

            //variable for location
            //must be instantiated inside method (principle of least privilege)
            //due to the fact we are supposed to click the 8 squares around loc
            //yet if loc was class scope, it would change at every doClick.
            //we don't want that.
            int loc = Integer.valueOf(c.getName());

            if(c.isSelected()){
                return;
            }

            //1. Disable the button
            //1a. Make transparent
            c.setContentAreaFilled(false);
            //1b. When clicked, make button do nothing
            if(((String)c.getClientProperty(myKey)).equals("Mine")){
                timer.stop();
            }
            c.removeActionListener(this);
            //1c. Remove the border
            c.setBorderPainted(true);
            //System.out.println(loc);

            //2. Display text
            //2a. Get the text from the key.
            key_value = (String)c.getClientProperty(myKey);
            //2b. Set the text to key_value
            //2ba. Set mines to mine icon
            if(key_value.equals("Mine")){
                try{
                    c.setIcon(
                            new ImageIcon(
                                    ImageIO.read(getClass()
                                            .getResource("anothermine.png"))));
                    } catch(IOException ex){
                        c.setText("Mine");
                }
            }
            else{
                c.setText(key_value);
            }
            //2c. Set the font to a respectable size
            if(!key_value.equals("Mine"))
                c.setFont(new Font("Arial", Font.BOLD, 24));
            //2d. Set the text color depending on number.
            if(key_value.equals("0"))
                c.setText(" ");
            if(key_value.equals("1"))
                c.setForeground(new Color(76, 37, 235));        
            if(key_value.equals("2"))
                c.setForeground(new Color(65, 238, 99));
            if(key_value.equals("3"))
                c.setForeground(new Color(235, 64, 64));
            if(key_value.equals("4"))
                c.setForeground(new Color(4, 29, 128));
            if(key_value.equals("5"))
                c.setForeground(new Color(111, 13, 13));
            if(key_value.equals("6"))
                c.setForeground(new Color(99, 201, 153));
            if(key_value.equals("7"))
                c.setForeground(new Color(0, 0, 0));        
            if(key_value.equals("8"))
                //the actual color is grey, but that won't do
                c.setForeground(new Color(222, 100, 236));
            //if we click on a Mine
            //render the game useless.
            if(key_value.equals("Mine")){
                for(int i = 0; i < 81; ++i){
                    if(buttons[i].getClientProperty(myKey).equals("Mine")){
                        buttons[i].doClick();
                    }
                }
                for(int i = 0; i < 81; ++i){
                    buttons[i].setEnabled(false);
                }
                try{
                    smile.setIcon(
                            new ImageIcon(
                                    ImageIO.read(getClass()
                                            .getResource("dead.gif"))));
                    } catch(IOException ex){
                        smile.setText("Oops!");
                }                
                return;
            }
            --SquaresLeft;
            //System.out.println("SquaresLeft is: " + SquaresLeft);
            if(SquaresLeft == 10){
                try{
                    smile.setIcon(
                            new ImageIcon(
                                    ImageIO.read(getClass()
                                            .getResource("winners.gif"))));
                    } catch(IOException ex){
                        c.setText("You won!");        
                }
                for(int i = 0; i < 81; ++i){
                    buttons[i].setEnabled(false);
                }
                timer.stop();
                return;
            }
            //If we click on a blank square that is a non-border square.
            /**
             * NOTE: I still need to make the method for border-squares.
             */
            if(key_value.equals("0") && 8 < loc && loc < 72 && loc % 9 != 0
                    && (loc + 1) % 9 != 0){

                //call doClick() on the eight buttons around this empty square.

                buttons[loc - 10].doClick();
                buttons[loc - 9].doClick();             
                buttons[loc - 8].doClick();
                buttons[loc - 1].doClick();
                buttons[loc + 1].doClick();             
                buttons[loc + 8].doClick();          
                buttons[loc + 9].doClick();           
                buttons[loc + 10].doClick();

            }
            //If we click on a blank square that is a corner square.
            else if(key_value.equals("0") && (loc == 0 || loc == 8 || loc == 72 
                    || loc == 80)){

                //call doClick() on the three buttons around this empty square.

                if(loc == 0){
                    buttons[1].doClick();
                    buttons[9].doClick();
                    buttons[10].doClick();
                }

                else if(loc == 8){
                    buttons[7].doClick();
                    buttons[16].doClick();
                    buttons[17].doClick();
                }

                else if(loc == 72){
                    buttons[73].doClick();
                    buttons[63].doClick();
                    buttons[64].doClick();
                }

                else if(loc == 80){
                    buttons[79].doClick();
                    buttons[71].doClick();
                    buttons[70].doClick();
                }

            }
            //the only possibility left is borders, not corners
            //left border
            else if(key_value.equals("0") && (loc % 9 == 0
                    || (loc + 1) % 9 == 0)){
                if(loc % 9 == 0){
                    buttons[loc - 9].doClick();
                    buttons[loc - 8].doClick();
                    buttons[loc + 1].doClick();
                    buttons[loc + 9].doClick();
                    buttons[loc + 10].doClick();
                }
            //right border
                if((loc + 1) % 9 == 0){
                    buttons[loc - 10].doClick();
                    buttons[loc - 9].doClick();
                    buttons[loc - 1].doClick();
                    buttons[loc + 8].doClick();
                    buttons[loc + 9].doClick();
                }
            }
            //top border
            else if(key_value.equals("0") && 0 < loc && loc < 9){
                buttons[loc - 1].doClick();
                buttons[loc + 1].doClick();
                buttons[loc + 8].doClick();
                buttons[loc + 9].doClick();
                buttons[loc + 10].doClick();
            }
            //bottom border
            else if(key_value.equals("0") && 72 < loc && loc < 80){
                buttons[loc - 1].doClick();
                buttons[loc + 1].doClick();
                buttons[loc - 8].doClick();
                buttons[loc - 9].doClick();
                buttons[loc - 10].doClick();
            }

            //begin the timer
            timer.start();
            //display data.
            this.validate();

        }

        public void clickedRight(MouseEvent event){

            c = (AbstractButton)event.getSource();

            arr = c.getActionListeners();

            //in case no ActionListeners are present
            if(arr.length == 0)
                return;

            //in case game is over
            if(!c.isEnabled()){
                return;
            }

            if(c.isSelected()){
                c.setText("");
                c.setForeground(Color.BLACK);
                c.setSelected(false);
                --flagCounter;
                mineNumbers.setText(Integer.toString(10 - flagCounter));
                return;
            }

            try{
                c.setIcon(
                        new ImageIcon(
                                ImageIO.read(getClass()
                                        .getResource("mine_flag.png"))));
                } catch(IOException ex){
                c.setText("Flag");
                c.setForeground(Color.RED);
            }            
            
            c.setSelected(true);
            ++flagCounter;
            mineNumbers.setText(Integer.toString(10 - flagCounter));

        }    

    }
}

public class WMinesweeper {

    private static JFrame frame;
    private static JMenuBar menuBar;
    private static JMenu menu;
    private static JPanel mainPanel;
    private static MineSmileTimer mine_smile_timer;
    private static MineSmileTimer.SquarePanel squarePanel;
    
    public static void main(String[] args) {
        
        
        frame = new JFrame("Windows Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create the menu bar.
        menuBar = new JMenuBar();
        
        //Build the first menu, followed by adding the menu to menuBar
        //this menu is also accessed by typing the key character "alt + H"
        menu = new JMenu("Help");
        menu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent e) {
                try {
                    try {
                        java.awt.Desktop.getDesktop()
                                .browse(new URI
                ("http://en.wikipedia.org/wiki/Minesweeper_%28video_game%29"));
                    } catch (IOException ex) {
                        Logger.getLogger(WMinesweeper.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                } catch (URISyntaxException ex) {
                    Logger.getLogger(WMinesweeper.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            
            @Override
            public void menuDeselected(MenuEvent e) {
                
            }
            
            //necessary to make non-abstract, but never actually called.
            @Override
            public void menuCanceled(MenuEvent e) {

            }
            
        });
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);
        
        //add the MenuBar to the JFrame
        frame.setJMenuBar(menuBar);
        
        //for now, beginner level. in future, should be based on an argument.
        /**
         * In fact, this will be based on a dialog (see picture with phone).
         */
        //create the main panel
        mainPanel = new JPanel();
        
        //set the layout of the main panel to a BorderLayout
        //north button for mine_smile_timer
        //south button for squares
        mainPanel.setLayout(new BorderLayout());
        
        //create a JPanel containing the mines, smiley, and timer.
        mine_smile_timer = new MineSmileTimer();
        //create a JPanel containing squares using SquarePanel
        squarePanel = mine_smile_timer.new SquarePanel();
        //Set panel containing mine, smile, and timer to the north button
        mainPanel.add(mine_smile_timer, BorderLayout.NORTH);
        //Set the panel containing the squares to center button
        mainPanel.add(squarePanel, BorderLayout.CENTER);
        //add the SquarePanel squarePanel to the JFrame frame
        frame.add(mainPanel);
        //set the default size to 300px*300px
        frame.setSize(600, 600);
        //set the frame to be visible.
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
}
