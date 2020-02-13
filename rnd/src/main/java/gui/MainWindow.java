package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.awt.Point;
import java.awt.Color;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import data.YUser;
import http.HttpRequest;
import http.RequestParser;

public class MainWindow extends JFrame implements ActionListener {

  private static final long serialVersionUID = 5975871377366251408L;

  private JTextPane   m_debugWnd;

  public MainWindow() {
    setSize(1200, 800);
    setTitle("Randomizer");
    setLocation(100, 100);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel contentPane = new JPanel();
    contentPane.setOpaque(true);
    contentPane.setLayout(null);

    setContentPane(contentPane);

    // debug messages
    m_debugWnd = new JTextPane();
    JScrollPane sp = new JScrollPane(m_debugWnd);

    sp.setSize(getWidth(), 200);
    sp.setLocation(0, getHeight() - sp.getHeight());
    sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    m_debugWnd.setEditable(true);
    m_debugWnd.setBorder(BorderFactory.createLoweredBevelBorder());
    m_debugWnd.setBackground(Color.DARK_GRAY);
    m_debugWnd.setMargin(new Insets(25, 25, 25, 25));
    m_debugWnd.setCharacterAttributes(StyleContext.getDefaultStyleContext()
                                                  .addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.WHITE), false);

    contentPane.add(sp);

    // settings
    JPanel settPane = new JPanel();
    settPane.setSize(300, 60);
    settPane.setLocation(10, 10);
    settPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    settPane.setLayout(null);
    contentPane.add(settPane);

    JLabel channelId = new JLabel("Channel ID");
    channelId.setSize(100, 20);
    channelId.setLocation(10, 10);
    settPane.add(channelId);

    JTextField chanId = new JTextField();
    chanId.setSize(150, 20);
    chanId.setLocation(channelId.getX() + channelId.getWidth(), channelId.getY());
    settPane.add(chanId);

    JLabel videoId = new JLabel("Video Id");
    videoId.setSize(100, 20);
    videoId.setLocation(channelId.getX(), channelId.getY() + channelId.getHeight() + 5);

    JTextField vidId = new JTextField();
    vidId.setSize(150, 20);
    vidId.setLocation(videoId.getX() + videoId.getWidth(), videoId.getY());
    settPane.add(videoId);
    settPane.add(vidId);

    // commentaries
    JPanel commPane = new JPanel();
    commPane.setLocation(10, settPane.getY() + settPane.getHeight() + 10);
    commPane.setSize(500, sp.getY() - commPane.getY() - 20);
    commPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

    JButton loadMsgBtn = new AButton(new Point(10, 10), "Load comments", null, "readmsg");
    loadMsgBtn.addActionListener(this);
    commPane.add(loadMsgBtn);
    contentPane.add(commPane);

    setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("readmsg")) {
      addDebugMessage("Reading comments...");

      try {
        String comms = HttpRequest.getMessages("ZrwqnZdjF6c");
        showUsers(RequestParser.collectUsers(comms));
      }
      catch(Exception ex) {
        addDebugMessage(ex.getMessage());
      }
    }
    else {
      addDebugMessage("Unknown button pressed!");
    }
  }

  public void addDebugMessage(String msg) {
    m_debugWnd.setCaretPosition(m_debugWnd.getDocument().getLength());
    m_debugWnd.replaceSelection(msg + "\n");
  }

  public void showUsers(HashSet<YUser> users) {
    for (YUser user : users)
      addDebugMessage(user.Name);
  }
}


final class AButton extends JButton {

  private static final long serialVersionUID = 8670252611919611249L;

  public AButton(Point Pos, String Text, ImageIcon Icon, String Action) {
    super(Text, Icon);
    setText(Text);
    setLocation(Pos);
    setSize(getPreferredSize());
    setActionCommand(Action);
  }
}
