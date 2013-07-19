
public class StartClass {
	
	public static void main(String[] args)
	{
		ConvexApplet app = new ConvexApplet();
		app.init();
		app.start();
		
		javax.swing.JFrame window = new javax.swing.JFrame("Convex Hull Applet");
		window.setSize(800, 800);
        window.setContentPane(app);
        window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        window.pack();              // Arrange the components.
        window.setVisible(true);    // Make the window visible. */
	}
}
