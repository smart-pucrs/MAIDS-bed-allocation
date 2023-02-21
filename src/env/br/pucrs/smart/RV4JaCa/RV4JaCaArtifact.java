// CArtAgO artifact code for project RV4JaCa

package br.pucrs.smart.RV4JaCa;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import cartago.Artifact;
import cartago.INTERNAL_OPERATION;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import jason.asSemantics.Message;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;

public class RV4JaCaArtifact extends Artifact implements IMonitor, IArtifact {
	RV4JaCaLog log = new RV4JaCaLog("RV4JaCaLog-" + (new Date()).getTime() + ".txt");
	private Gson gson = new Gson();
	WebsocketClientEndpoint c;

	void init(String uri) {
		System.out.println("[RV4JaCa] Artifact enabled");
		SnifferCentralised.setListener(this);
		WebsocketClientEndpoint.setListener(this);
		try {
			System.out.println("[RV4JaCa] URI: " + uri);
            // open websocket
			this.c = new WebsocketClientEndpoint(new URI(uri));
			    this.c.connect();
			    
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
	}

	@OPERATION
	void isEnabled(OpFeedbackParam<String> data) {
		data.set("Yes");
	}
	
	

	@Override
	public synchronized void propagateToMonitor(Message m) {
		
		String res = messageToJson(m);
		this.c.send(res);
		log.add(res);
	}

	String messageToJson(Message m) {
		
		MsgSent msgSent = new MsgSent();
		msgSent.setTime(new Date());
		msgSent.setMsgId(m.getMsgId());
		if (m.getInReplyTo() == null) {
			msgSent.setIsReply("nirt");
		} else {
			msgSent.setIsReply(m.getInReplyTo());
		}
		msgSent.setPerformative(m.getIlForce());
		msgSent.setSender(m.getSender());
		msgSent.setReceiver(m.getReceiver());
		msgSent.setContent(mapContent((Term) m.getPropCont()));
//		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//		System.out.println(m.getPropCont());
//		System.out.println(gson.toJson(msgSent));
		return gson.toJson(msgSent);
	}

	HashMap<String, Object> mapContent(Term c) {
		HashMap<String, Object> content = new HashMap<String, Object>();
		if (c.isLiteral()) {
			content.putAll(literalToJSON((Literal) c));
		} else {
			content.putAll((Map<? extends String, ? extends Object>) content.entrySet());
		}

//		System.out.println("getAsJSON");
//		return l.getAsJSON("");
		return content;
	}

	public HashMap<String, Object> literalToJSON(Literal l) {
		HashMap<String, Object> content = new HashMap<String, Object>();
		content.put("name", l.getFunctor());
		content.put("isNegated", l.negated());
		if (l.hasTerm()) {
			int i = 0;
			for (Term term : l.getTerms()) {
				if (term.isAtom()) {
//					System.out.println("Atom");
//					System.out.println(term);
					if (!((Literal) term).getFunctor().equals(l.getFunctor())) {
						if ((!((Literal) term).getFunctor().equals("default"))) {
							i++;
							content.put("prop" + i, term);
						}
					}
				} else if (term.isLiteral()) {
//					System.out.println("literal");
//					System.out.println(term);
					if (!((Literal) term).getFunctor().equals(l.getFunctor())) {
						if ((!((Literal) term).getFunctor().equals("default"))) {
							i++;
							content.put("prop" + i, literalToJSON((Literal) term));
						}
					}

				} else if (term.isList()) {
//					System.out.println("list");
//					System.out.println(term);
					List<Object> lt = listTermToJSON((ListTermImpl) term);

					i++;
					HashMap<String, Object> arrObject = new HashMap<String, Object>();
					arrObject.put("length", lt.size());
					if (lt.size() > 0) {
						arrObject.put("arr", lt);
					}
					content.put("prop" + i, arrObject);

				} else {
//					System.out.println("else");
//					System.out.println(term);
					i++;
					content.put("prop" + i, term.toString());
				}

			}
		}
		return content;
	}

	public List<Object> listTermToJSON(ListTermImpl terms) {
//		List<HashMap<String, Object>> contentList = new ArrayList<HashMap<String, Object>>();

		List<Object> content = new ArrayList<Object>();
		int i = 0;
		for (Term t : terms) {
			if (t.isAtom()) {
				if ((!((Literal) t).getFunctor().equals("default"))) {

//					System.out.println("Atom List");
//					System.out.println(t);
					i++;
					content.add(t);
				}
			} else if (t.isLiteral()) {
//				System.out.println("Literal List");
//				System.out.println(t);
				Literal l = (Literal) t;
				i++;
				content.add(literalToJSON(l));
			} else if (t.isList()) {
				ListTermImpl lt = (ListTermImpl) t;
				List<Object> ltf = listTermToJSON(lt);
				if (ltf.size() > 0) {
//					System.out.println("List List");
//					System.out.println(t);
					i++;
					content.add(ltf);
				}
			} else {
//				System.out.println("else List");
//				System.out.println(t);
				i++;
				content.add(t.getAsJSON(""));
			}
//			contentList.add(content);
		}

		return content;
	}

	@Override
	public void informViolation(MsgSent m) {
		execInternalOp("createBelief", m);
	}
	
	@INTERNAL_OPERATION
	void createBelief(MsgSent m) {
        defineObsProperty("violation", 
        		new StringTermImpl(m.getTime().toString()),
        		new StringTermImpl(m.getMsgId()),
        		new StringTermImpl(m.getIsReply()),
        		new Atom(m.getPerformative()),
        		new Atom(m.getSender()),
        		new Atom(m.getReceiver()),
        		new Atom(m.getVerdict()));
	}
}
