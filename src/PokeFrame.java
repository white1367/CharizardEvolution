/*
康瀚中
105403018
資管2A
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PokeFrame extends JFrame 
{
	private PokeSerializable pokeSerializable;
	private static FileOutputStream fos;
	private static ObjectOutputStream output;
	private static JFileChooser jfc = new JFileChooser();
	private static FileInputStream fis;
	private static ObjectInputStream input;
	
	private Scanner scanner;
	private File file;
	private JLabel pokemonLabel;
	private URL[] imageURL;
	private Icon[] pokemon;
	private String[] Monster;
	private int[] evolveCandy;
	private int pokemonstatus;
	private JPanel tools;
	private JTextField nickName; 
	private JPanel[] toolPanel;
	private JButton giveCandy;
	private JLabel candyNum;
	private int candy;
	
	private JButton openGame;
	private JButton save;
	private JButton saveAs;
	private JButton mega;
	
	private JLabel gameStatus;
	public PokeFrame()
	{
		pokemonstatus=0;
		candy=0;
		Monster =new String[3];
		evolveCandy =new int[3];
		imageURL = new URL[3];
		try 
		{
			file = new File("src/pokemon.txt");//讀取pokemon.txt
			System.out.println(file.getAbsolutePath());
			pokemon = new ImageIcon[3];
			scanner = new Scanner(file);
			scanner.next();//跳過第一行
			//取得txt檔的圖片名稱和糖果數
			Monster[0] = scanner.next();
			imageURL[0] = PokeFrame.class.getResource(Monster[0]);
			pokemon[0] = new ImageIcon(imageURL[0]);
			
			evolveCandy[0] = Integer.parseInt(scanner.next());
			
			Monster[1] = scanner.next();
			imageURL[1] = PokeFrame.class.getResource(Monster[1]);
			pokemon[1] = new ImageIcon(imageURL[1]);
			
			evolveCandy[1] = Integer.parseInt(scanner.next());
			
			Monster[2] = scanner.next();
			imageURL[2] = PokeFrame.class.getResource(Monster[2]);
			pokemon[2] = new ImageIcon(imageURL[2]);
			evolveCandy[2] = evolveCandy[1];
			
			scanner.close();
			file = null;
		} 
		catch(IOException ioexception)
		{
			ioexception.printStackTrace();
		}
		//
		setLayout(new BorderLayout());
		pokemonLabel = new JLabel();
		add(pokemonLabel, BorderLayout.NORTH);
		pokemonLabel.setIcon(pokemon[pokemonstatus]);	
		
		tools = new JPanel();
		add(tools, BorderLayout.SOUTH);
		tools.setLayout(new GridLayout(4,1));
		nickName = new JTextField("set your nickname");
		tools.add(nickName);
		toolPanel = new JPanel[3];
		
		toolPanel[0] = new JPanel();
		tools.add(toolPanel[0]);
		//toolLabel[0].setBorder(BorderFactory.createLineBorder(Color.BLACK));
		giveCandy = new JButton("Give Candy");
		giveCandy.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					if(candy < evolveCandy[1])
					{
						candy++;
						candyNum.setText(candy+"/"+evolveCandy[pokemonstatus]);
						if(candy == evolveCandy[pokemonstatus])
						{
							if(pokemonstatus == 0 )
							{	
								pokemonstatus++;
								candy=0;
								pokemonLabel.setIcon(pokemon[pokemonstatus]);
								candyNum.setText(candy+"/"+evolveCandy[pokemonstatus]);
								JOptionPane.showMessageDialog(null, "Your monster is evolved!");
							}
							else if(pokemonstatus ==1)
							{
								pokemonstatus++;
								pokemonLabel.setIcon(pokemon[pokemonstatus]);
								JOptionPane.showMessageDialog(null, "Congratulation!! Your monster has final evolved!");
								
								mega.setVisible(true);//讓 mega進化的按鈕顯現
								JOptionPane.showMessageDialog(null, "你的神奇寶貝可以Mega進化囉!"+"\n"+"按下Mega Evolution選擇進化型態");
							}
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null,"Your pokemon has achieve the highest level!");
					}
				}	
			}
		);
		toolPanel[0].add(giveCandy);
		candyNum = new JLabel(candy+"/"+evolveCandy[pokemonstatus]);
		toolPanel[0].add(candyNum);
		
		
		toolPanel[1] = new JPanel();
		tools.add(toolPanel[1]);
		openGame = new JButton("Open Game");
		openGame.addActionListener(new openGameHandler());
		toolPanel[1].add(openGame);
		save = new JButton("Save");
		save.addActionListener(new saveHandler());
		toolPanel[1].add(save);
		saveAs = new JButton("Save As");
		saveAs.addActionListener(new saveAsHandler());
		toolPanel[1].add(saveAs);
		
		toolPanel[2] = new JPanel();
		toolPanel[2].setLayout(new BorderLayout());
		tools.add(toolPanel[2]);
		gameStatus = new JLabel("New File");
		toolPanel[2].add(gameStatus,BorderLayout.WEST);
		mega = new JButton("Mega Evolution?");
		mega.setVisible(false);
		mega.addActionListener(new megaHandler());
		toolPanel[2].add(mega,BorderLayout.EAST);
		
	}
	private class openGameHandler implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			jfc.setCurrentDirectory(null);
			int result = jfc.showOpenDialog(null);
			if(result == JFileChooser.APPROVE_OPTION)
			{
				try 
				{
				
					File openfile = jfc.getSelectedFile();
					input = new ObjectInputStream(Files.newInputStream(Paths.get(openfile.getAbsolutePath())));
					pokeSerializable = (PokeSerializable) input.readObject();
					gameStatus.setText(openfile.getAbsolutePath());
				} 
				catch (IOException ioException) 
				{
					System.err.println("IOException");
				} 
				catch (ClassNotFoundException classNotFoundException)
				{
					System.err.println("This is not the file been saved");
				}
				//System.out.println(pokeSerializable.getNickname()+"/"+pokeSerializable.getMonster()+"/"+pokeSerializable.getCandy());
				candy = pokeSerializable.getCandy();
				nickName.setText(pokeSerializable.getNickname());
				for(int i = 0; i<Monster.length;i++)
				{
					if(pokeSerializable.getMonster().equals(Monster[i]))
					{
						pokemonstatus = i;
						break;
					}
				}
				pokemonLabel.setIcon(pokemon[pokemonstatus]);
				candyNum.setText(candy+"/"+evolveCandy[pokemonstatus]);
				if(pokemonstatus == 2)
				{
					mega.setVisible(true);
				}
			}
			
		}
		
	}
	private class saveHandler implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			pokeSerializable = new PokeSerializable(nickName.getText(),Monster[pokemonstatus],candy);
			if(gameStatus.getText().equals("New File"))
			{
				int result = jfc.showSaveDialog(null);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					File file = jfc.getSelectedFile();
					try 
					{
						fos = new FileOutputStream(file.getAbsolutePath());
						output = new ObjectOutputStream(fos);
						output.writeObject(pokeSerializable);
						output.close();
					}
					catch (FileNotFoundException fileNotFoundException) 
					{
						System.err.println("File not found");
					}
					catch (IOException ioException)
					{
						System.err.println("Error opening file");
					}
					gameStatus.setText(file.getAbsolutePath());
				}
				
			}
			else
			{
				File file = new File(gameStatus.getText());
				try 
				{
					fos = new FileOutputStream(file.getAbsolutePath());
					output = new ObjectOutputStream(fos);
					output.writeObject(pokeSerializable);
					output.close();
				}
				catch (FileNotFoundException fileNotFoundException) 
				{
					System.err.println("File not found");
				}
				catch (IOException ioException)
				{
					System.err.println("Error opening file");
				}
				gameStatus.setText(file.getAbsolutePath());
			}
		}
		
	}
	private class saveAsHandler implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			pokeSerializable = new PokeSerializable(nickName.getText(),Monster[pokemonstatus],candy);
			int result = jfc.showSaveDialog(null);
			if(result == JFileChooser.APPROVE_OPTION)
			{
				File file = jfc.getSelectedFile();
				try 
				{
					fos = new FileOutputStream(file.getAbsolutePath());
					output = new ObjectOutputStream(fos);
					output.writeObject(pokeSerializable);
					output.close();
				}
				catch (FileNotFoundException fileNotFoundException) 
				{
					System.err.println("File not found");
				}
				catch (IOException ioException)
				{
					System.err.println("Error opening file");
				}
			}
			
		}
		
	}
	private class megaHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event) 
		{
			String[] option = {"X mega","Ymega"};
			int xy =JOptionPane.showOptionDialog(null,"你要將噴火龍X mega進化還是Y mega進化?"+"\n"+"注意!Mega進化皆為暫時的","Mega Evolution", 0,JOptionPane.PLAIN_MESSAGE,new ImageIcon(getClass().getResource("Mega進化.png")), option,null);
			if(xy == 0)
			{
				pokemonLabel.setIcon(new ImageIcon(getClass().getResource("噴火龍Xmega.png")));
			}
			else if(xy == 1)
			{
				pokemonLabel.setIcon(new ImageIcon(getClass().getResource("噴火龍Ymega.png")));
			}
		}
	}
	
}