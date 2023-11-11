import java.util.Random;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.GridLayout;

public class MultiplicationTableGame {
	private Random rnd = new Random();
	private GuiBuilder gui = new GuiBuilder();
	private int a, b, trueCount, falseCount;
	private long startTime = System.currentTimeMillis();
	private long exStartTime = System.currentTimeMillis();
	
	public MultiplicationTableGame() {
		makeExmaple();
		makeButtons();
		gui.setActive(false);
	}
	
	void makeExmaple() {
		a = rnd.nextInt(1, 9);
		b = rnd.nextInt(1, 9);
		gui.setExample(a, b);
	}
	
	void makeButtons() {
		int trueAnswerNum = rnd.nextInt(gui.getAnswersSize()-1);
		for (int i = 0; i < gui.getAnswersSize(); i++) {
			if (trueAnswerNum == i)
				gui.setAnswer(i, Integer.toString(a*b));
			else
				gui.setAnswer(i, Integer.toString(rnd.nextInt(Math.abs(a*b-10),a*b+10)));
		}
	}

	public void checkExample(JButton btn) {
		
		int exTime = (int) (System.currentTimeMillis() - exStartTime)/1000;
		exStartTime = System.currentTimeMillis();
		String text;
		if (Integer.parseInt(btn.getText()) == a*b) {
			text = "Верно!";
			trueCount++;
			gui.addAnswer(a, b, Integer.parseInt(btn.getText()), text, exTime);
			makeExmaple();
			makeButtons();
		} else {
			falseCount++;
			text = "Ошибка!";
			gui.addAnswer(a, b, Integer.parseInt(btn.getText()), text, exTime);
		}
		if ((trueCount + falseCount) % 10 == 0) {
			int time = (int) (System.currentTimeMillis() - startTime) / 1000;
			gui.setTimeStatus("10 примеров за " + time + " сек.");
			gui.setActive(false);
		}
		gui.refreshStatus(text, trueCount, falseCount);
	}
	
	public void reset() {
		startTime = System.currentTimeMillis();
		exStartTime = System.currentTimeMillis();
		trueCount = 0;
		falseCount = 0;
		gui.clearLabels();
		gui.setActive(true);
		makeExmaple();
		makeButtons();
	}
	
	public static void main(String[] args) {
		new MultiplicationTableGame();
	}

	class GuiBuilder extends JFrame {
		private static final long serialVersionUID = 1L;
		private JLabel example = new JLabel();
		private JLabel status = new JLabel("Hello my friend, and welcome!");
		private JLabel timeStatus = new JLabel();
		private JLabel answerStatus = new JLabel();
		private JTextArea answerList = new JTextArea();
		private ArrayList<JButton> answers = new ArrayList<>();
		private JButton reset = new JButton("Учить!");
		
		public void setExample(int a, int b) {
			example.setText(a + "x" + b + "=");
		}
		
		public void setStatus(String text) {
			status.setText(text);
		}
		
		public void setTimeStatus(String text) {
			timeStatus.setText(text);
		}
		
		public void setAnswer(int i, String text) {
			answers.get(i).setText(text);
		}
		
		public int getAnswersSize() {
			return answers.size();
		}
		
		public void refreshStatus(String text, int trueCount, int falseCount) {
			status.setText(text);
			answerStatus.setText("Правильно " + trueCount + " из " + (trueCount + falseCount));
		}
		
		public void setActive(boolean b) {
			for (JButton btn : answers) {
				btn.setEnabled(b);
			}
			reset.setEnabled(!b);
		}
		
		public void addAnswer(int a, int b, int c, String text, int time) {
			answerList.append(a + "x" + b + "=" + c + " " + text + " " + time + "сек.\n");
		}
		public void clearLabels() {
			status.setText("");
			timeStatus.setText("");
			answerStatus.setText("");
			answerList.setText("");
		}
		
		GuiBuilder () {
			setTitle("MT");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setLayout(new GridLayout(3, 1, 0, 10));
			JPanel answerPanel = new JPanel();
			answerPanel.setLayout(new GridLayout(3, 2, 5, 5));
			for (int i = 0; i < 6; i++) {
				JButton btn = new JButton(Integer.toString(i));
				btn.setFont(new Font("Sans", NORMAL, 20));
				btn.addActionListener(event -> checkExample(btn));
				answers.add(btn);
				answerPanel.add(answers.get(i));
			}
			JPanel statusPanel = new JPanel();
			statusPanel.setLayout(new GridLayout(4, 1, 0, 10));
			statusPanel.add(timeStatus);
			statusPanel.add(status);
			statusPanel.add(answerStatus);
			reset.setEnabled(false);
			reset.addActionListener(event -> reset());
			statusPanel.add(reset);
			
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new GridLayout(1, 3, 10, 0));
			mainPanel.add(example);
			mainPanel.add(answerPanel);
			mainPanel.add(statusPanel);
			
			example.setFont(new Font("Sans", NORMAL, 70));
			JLabel title = new JLabel("Учи таблицу, Вика!");
			title.setFont(new Font("Sans", NORMAL, 60));
			getContentPane().add(title);
			getContentPane().add(mainPanel);
			getContentPane().add(new JScrollPane(answerList));
			pack();
			setVisible(true);
		}
	}
}
