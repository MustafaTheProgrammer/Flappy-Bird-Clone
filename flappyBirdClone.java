import java.util.ArrayList;
import java.util.Random;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

class flappyBirdClone extends JFrame implements KeyListener{
    public static char[][] pixels = new char[18][24];
    public static int playerx = 2;
    public static float playery = -11.0f;
    public static float playeryv = 0.0f;
    boolean spaceKey;
    boolean runGame = true;
    int pipeSpawnTimer = 0;
    int randomBotttomPipeY;
    int score = 0;
    public static ArrayList<Integer> pipes = new ArrayList<Integer>();
    Random random = new Random();

    public flappyBirdClone(){
        this.setTitle("Flappy Bird Clone");
        this.setSize(100,100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setVisible(true);
        gameLoop();
    }

    private void gameLoop(){
        while (runGame == true){
            //clear screen
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            //reset buffer
            for (int row = 0; row < 18; row++){
                for (int col = 0; col < 24; col++){
                    pixels[row][col] = '.';
                }
            }

            //handle player
            playeryv -= 0.25;
            playery += playeryv;
            if (spaceKey){
                playeryv = 1.25f;
            }
            if (playery < -18){
                runGame = false;
            }
            if (playery > -2){
                playery = -2;
                playeryv = 0;
            }
            drawRect(playerx, playery, 3, 3, '*');

            //handle pipe
            pipeSpawnTimer++;
            if (pipeSpawnTimer == 20){
                randomBotttomPipeY = random.nextInt(3)-2;
                addPipe(23, -17 + randomBotttomPipeY, 4, 5);
                addPipe(23, -17 + randomBotttomPipeY + 13, 4, 9);
                pipeSpawnTimer = 0;
            }

            for (int i = 0; i < pipes.size(); i += 8){
                pipes.set(i, pipes.get(i) - 1);
                pipes.set(i+4, pipes.get(i+4) - 1);
                if (playerx == pipes.get(i) + 2){
                    score++;
                }
            }

            for (int i = 0; i < pipes.size(); i += 4){
                if (playerx <= pipes.get(i) + pipes.get(i+2)-1 && playerx + 2 >= pipes.get(i) && playery <= pipes.get(i+1) + pipes.get(i+3)-1 && playery + 2 >= pipes.get(i+1)){
                    runGame = false;
                }
            }

            for (int i = pipes.size() - 8; i >= pipes.size(); i -= 8){
                if (pipes.get(Math.abs(i)) == -3){
                    pipes.remove(Math.abs(i));
                    pipes.remove(Math.abs(i+1));
                    pipes.remove(Math.abs(i+2));
                    pipes.remove(Math.abs(i+3));
                    pipes.remove(Math.abs(i+4));
                    pipes.remove(Math.abs(i+5));
                    pipes.remove(Math.abs(i+6));
                    pipes.remove(Math.abs(i+7));
                }
            }
            
            for (int i = 0; i < pipes.size(); i += 8){
                drawRect(pipes.get(i), pipes.get(i+1), pipes.get(i+2), pipes.get(i+3), '#');
                drawRect(pipes.get(i+4), pipes.get(i+5), pipes.get(i+6), pipes.get(i+7), '#');
            }
            
            //render
            System.out.println("Score: " + score);
            for (int row = 0; row < 18; row++){
                for (int col = 0; col < 24; col++){
                    System.out.print(pixels[row][col]);
                }
                System.out.println();
            }

            //delay
            try{
                Thread.sleep(100);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    //functions  
    public static void addPipe(int x, int y, int width, int height){
        pipes.add(x);
        pipes.add(y);
        pipes.add(width);
        pipes.add(height);
    }
    
    public static void setPixel(int x, float y, char sym){
        if (x >= 0 && x <= 23 && y >= -17 && y <= 0){
            pixels[Math.round(Math.abs(y))][x] = sym;
        }
    }

    public static void drawRect(int x, float y, int width, int height, char sym){
        for (int a = x; a < x+width; a++){
            for (int b = Math.round(y); b < Math.round(y)+height; b++){
                setPixel(a, b, sym);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e){
        //not used
    }
    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == 32){
            spaceKey = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e){
        if (e.getKeyCode() == 32){
            spaceKey = false;
        }
    }

    public static void main(String[] args){
        new flappyBirdClone();
    }
}