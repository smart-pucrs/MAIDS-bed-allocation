package br.pucrs.smart.RV4JaCa;

import jason.RevisionFailedException;
import jason.architecture.AgArch;
import jason.asSemantics.Message;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Structure;
import jason.infra.centralised.CentralisedAgArch;
import jason.infra.centralised.MsgListener;
import jason.infra.centralised.RunCentralisedMAS;

import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 * Customisation of an agent architecture to sniff the MAS with
 * Centralised infrastructure.
 *
 * @author Jomi
 */
public class SnifferCentralised extends AgArch implements MsgListener {
    static IMonitor rv4jaca = null;
    
	public static void setListener(IMonitor m) {
        System.out.println("[RV4JaCa] - Sniffer active");
		rv4jaca = m;
	}
    @Override
    public void init() {
        if (RunCentralisedMAS.getRunner() != null) {// is it the case of centralised platform being used
            CentralisedAgArch.addMsgListener(this);
        }
    }

    // method called-back when some message is exchanged
    public synchronized void msgSent(Message m) {
    	System.out.println("*******************************************************" + m.getMsgId());
        //getTS().getLogger().fine("Message:"+m);

        // add a belief in the agent mind
        // format: msgSent(time(YY,MM,DD,HH,MM,SS),id,irt,ilf,sender,receiver,content)
    	
    	rv4jaca.propagateToMonitor(m);
//        Calendar now = new GregorianCalendar();
//        Structure p = ASSyntax.createStructure("time",
//                                               ASSyntax.createNumber(now.get(Calendar.YEAR)),
//                                               ASSyntax.createNumber(now.get(Calendar.MONTH)),
//                                               ASSyntax.createNumber(now.get(Calendar.DAY_OF_MONTH)),
//                                               ASSyntax.createNumber(now.get(Calendar.HOUR)),
//                                               ASSyntax.createNumber(now.get(Calendar.MINUTE)),
//                                               ASSyntax.createNumber(now.get(Calendar.SECOND)));
//        Literal e = ASSyntax.createLiteral("msg_sent", p);
//
//        e.addTerm(new StringTermImpl(m.getMsgId()));
//        if (m.getInReplyTo() == null) {
//            e.addTerm(new Atom("nirt"));
//        } else {
//            e.addTerm(new StringTermImpl(m.getInReplyTo()));
//        }
//        e.addTerm(new Atom(m.getIlForce()));
//        e.addTerm(new Atom(m.getSender()));
//        e.addTerm(new Atom(m.getReceiver()));
//        e.addTerm(new StringTermImpl(m.getPropCont().toString()));
//        try {
//            getTS().getAg().addBel(e);
//        } catch (RevisionFailedException e1) {
//            e1.printStackTrace();
//        }
    }
}
