import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test {
	

	public Test() {

	}

	public static void main(String[] args) throws Exception {
		Synthesizer synth = MidiSystem.getSynthesizer();
		synth.open();
		
		final MidiChannel[] mc = synth.getChannels();
		Instrument[] instr = synth.getDefaultSoundbank().getInstruments();
		final Instrument ins = instr[0];
		synth.loadInstrument(ins);
		
		JFrame frame = new JFrame("Sound1");                
		JPanel pane = new JPanel();                         
		JButton button1 = new JButton("Click me!");            
		frame.getContentPane().add(pane);                   
		pane.add(button1);                                     
		frame.pack();                                       
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
		final int base = 30;
		final float time = 0.25f;
		

		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long last = System.currentTimeMillis();
				for(int i = 0; i < 8; i++){
					mc[4].programChange(ins.getPatch().getProgram());
					mc[4].noteOn(base + i * 8,20);
				}
				
				while(System.currentTimeMillis() - last < time * 1000){
					
				}
				
				mc[4].allNotesOff();
			}
		});
	}
}
