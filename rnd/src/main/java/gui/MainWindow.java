package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.json.JSONException;

import data.YComment;
import data.YUser;
import http.HttpRequest;
import http.RequestParser;

public class MainWindow extends JFrame implements ActionListener {

  private static final long serialVersionUID = 5975871377366251408L;

  private JTextPane m_comms;
  private JTextPane m_debugWnd;
  private JTextField m_videoId;

  public MainWindow() {
    setSize(1200, 800);
    setTitle("Randomizer");
    setLocation(100, 0);
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
    m_debugWnd.setCharacterAttributes(StyleContext.getDefaultStyleContext().addAttribute(SimpleAttributeSet.EMPTY,
        StyleConstants.Foreground, Color.WHITE), false);

    contentPane.add(sp);

    // settings
    JPanel settPane = new JPanel();
    settPane.setSize(450, 40);
    settPane.setLocation(10, 10);
    settPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    settPane.setLayout(null);
    contentPane.add(settPane);

    JLabel videoId = new JLabel("Video Id");
    videoId.setSize(100, 20);
    videoId.setLocation(10, 10);

    m_videoId = new JTextField();
    m_videoId.setText("ZrwqnZdjF6c");
    m_videoId.setSize(150, 20);
    m_videoId.setLocation(videoId.getX() + videoId.getWidth(), videoId.getY());
    settPane.add(videoId);
    settPane.add(m_videoId);

    JButton loadMsgBtn = new AButton(new Point(m_videoId.getX() + m_videoId.getWidth() + 10, m_videoId.getY() - 4),
        "Load comments", null, "readmsg");
    loadMsgBtn.addActionListener(this);
    settPane.add(loadMsgBtn);

    // commentaries
    m_comms = new JTextPane();
    JScrollPane commScroller = new JScrollPane(m_comms);
    commScroller.setLocation(10, settPane.getY() + settPane.getHeight() + 10);
    commScroller.setSize(new Dimension(getWidth() / 2, sp.getY() - commScroller.getY() - 20));
    commScroller.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    contentPane.add(commScroller);

    JTextArea comment = new JTextArea();
    comment.setLineWrap(true);
    comment.setWrapStyleWord(true);

    setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("readmsg")) {
      addDebugMessage("Reading comments...");

      try {
        String comms = HttpRequest.getMessages(m_videoId.getText().trim());
        showUsers(RequestParser.collectUsers(comms));
        showComms(comms);
      } catch (Exception ex) {
        addDebugMessage(ex.getMessage());
      }
    } else {
      addDebugMessage("Unknown button pressed!");
    }
  }

  public void addDebugMessage(String msg) {
    m_debugWnd.setCaretPosition(m_debugWnd.getDocument().getLength());
    m_debugWnd.replaceSelection(msg + "\n");
  }

  public void showUsers(Set<YUser> users) {
    for (YUser user : users)
      addDebugMessage(user.Name);
  }

  public void showComms(String comms) {
    try {
      for (int i = 0; i < 1000; ++i) {
        YComment comm = RequestParser.getComment(comms, i);
        addComm(comm);
      }
    } catch (JSONException ex) {
      // no more comments
      m_comms.setCaretPosition(0);
      m_comms.setEditable(false);
    }
  }

  private void addComm(YComment comm) {
    Document doc = m_comms.getStyledDocument();
    SimpleAttributeSet set = new SimpleAttributeSet();

    StyleConstants.setBold(set, true);
    StyleConstants.setForeground(set, Color.BLUE);
    m_comms.setCharacterAttributes(set, true);

    try {
      doc.insertString(doc.getLength(), comm.User.Name, set);

      Date date = new SimpleDateFormat("yyyy-mm-dd").parse(comm.Published);
      set = new SimpleAttributeSet();
      StyleConstants.setForeground(set, Color.DARK_GRAY);
      StyleConstants.setItalic(set, true);
      StyleConstants.setFontSize(set, 10);
      doc.insertString(doc.getLength(), "\t" + new SimpleDateFormat("dd/mm/yyyy").format(date) + "\n", set);

      set = new SimpleAttributeSet();
      doc.insertString(doc.getLength(), comm.Text + "\n\n", set);
    } catch (BadLocationException e) {
      e.printStackTrace();
      addDebugMessage(e.getMessage());
    } catch (ParseException e) {
      e.printStackTrace();
      addDebugMessage(e.getMessage());
    }
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
