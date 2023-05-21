import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends Panel implements Runnable //Thread class
{
    int width=1000;
    int height=(int)(width*(0.555));
    Dimension screen=new Dimension(width,height);

    int Paddle_Width=25;
    int Paddle_Height=100;
    int ball_diameter=20;

    Image images;

    Graphics graphic;

    Paddle p1,p2;//2 paddles created

    Ball ball;//1 ball created

    Score score=new Score(width,height);//Scoreboard created

    //Over over;

    Thread GameThread;
    GamePanel()
    {
        setPreferredSize(screen);
        GameThread=new Thread(this);

        addKeyListener(new AL());//Similar to ActionListener

        setFocusable(true);

        GameThread.start();

        newPaddle();
        newBall();
    }


    private void newBall()
    {
        Random random=new Random(); //ball will start moving random direction
        ball=new Ball(width/2,random.nextInt(height-ball_diameter),ball_diameter,ball_diameter);
    }

    private void newPaddle() //Paddles Positions
    {
        p1=new Paddle(0,(height-Paddle_Height)/2,Paddle_Width,Paddle_Height,1);
        p2=new Paddle(width-Paddle_Width,(height-Paddle_Height)/2,Paddle_Width,Paddle_Height,2);
    }
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        images=createImage(getWidth(),getHeight());//Create Image with width and height
        graphic=images.getGraphics();//Take the image graphics and try to draw the image
        draw(graphic);//Draw using the provided graphics
        g.drawImage(images,0,0,this);
    }
    private void draw(Graphics g)
    {
        p1.draw(g);
        p2.draw(g);
        ball.draw(g);
        score.draw(g);
    }
    @Override
    public void run() //used to create a thread
    {
        long lastTime = System.nanoTime();//Give the system timing in nanoseconds
        double NumberOfTicks = 60;//60 ticks in 1 second
        double ns=1000000000/(NumberOfTicks);//Number of ns in 1 ticks(1/60 seconds)
        double del=0;
        while(true)
        {
            long Now=System.nanoTime();
            del+=(Now - lastTime)/(ns);
            lastTime=Now;
            if (del>=1)
            {
                repaint();//Calls the paint function
                move();
                CheckCollision();
                del--;
            }
        }
    }

    private void CheckCollision()
    {
        if(ball.y<=0) //Upper Boundary
        {
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y>=height-ball_diameter) //Lower Boundary
        {
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.intersects(p1)) //ball touches paddle p1
        {
            ball.xVelocity=-ball.xVelocity; //change the ball direction
            ball.xVelocity++; // increase the speed
            if(ball.yVelocity>0)
            {
                ball.yVelocity++;
            }
            else
            {
                ball.yVelocity--;
            }
            ball.setYDirection(ball.yVelocity);
            ball.setXDirection(ball.xVelocity);
        }

        if(ball.intersects(p2)) //ball touches paddle p2
        {
            ball.xVelocity=-ball.xVelocity; //change the ball direction
            ball.xVelocity--; // increase the speed
            if(ball.yVelocity>0)
            {
                ball.yVelocity++;
            }
            else
            {
                ball.yVelocity--;
            }
            ball.setYDirection(ball.yVelocity);
            ball.setXDirection(ball.xVelocity);
        }

        if(p1.y<=0)
        {
            p1.y=0;
        }
        if(p1.y>=height-Paddle_Height)
        {
            p1.y=height-Paddle_Height;
        }
        if(p2.y<=0)
        {
            p2.y=0;
        }
        if(p2.y>=height-Paddle_Height)
        {
            p2.y=height-Paddle_Height;
        }
        if(ball.x>=width-ball_diameter)
        {
            //ball.xVelocity=-ball.xVelocity;
            newBall(); //New ball will be printed
            newPaddle(); //Paddle will be at previous location
            score.player1++;
            //display();
        }
        if(ball.x<=0)
        {
            newBall(); //New ball will be printed
            newPaddle(); //Paddle will be at previous location
            score.player2++;
            //display();
        }

    }
    private void move()
    {
        p1.move();
        p2.move();
        ball.move();
    }

    public class AL extends KeyAdapter
        {
            @Override
            public void keyPressed(KeyEvent e)//When a key is pressed
            {
                //super.keyPressed(e);
                p1.keyPressed(e);
                p2.keyPressed(e);
            }
            @Override
            public void keyReleased(KeyEvent e)//When a key is released
            {
                //super.keyReleased(e);
                p1.keyReleased(e);
                p2.keyReleased(e);
            }
        }
}