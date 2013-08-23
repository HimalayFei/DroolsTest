package com.encima;
 
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.encima.dwc.DarwinCore;
import com.encima.dwc.Identification;
import com.encima.dwc.ImageSet;
import com.encima.dwc.Occurrence;
import com.encima.utils.FileTools;
 
public class DroolRunner {
 
    private static KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    private static Collection<KnowledgePackage> pkgs;
    private static KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
    private static StatefulKnowledgeSession ksession;
 
    public static void main(final String[] args) {
 
        initDrools();
        initMessageObject();
        addDWC();
        fireRules();
 
    }
 
    private static void initDrools() {
    	String rule = FileTools.readFileAsString("/home/encima/development/java/drools/DroolsTest/src/main/rules/Sample.drl");
		Resource myResource = ResourceFactory.newReaderResource((Reader) new StringReader(rule));
        kbuilder.add(myResource, ResourceType.DRL);
        // Check the builder for errors
        if ( kbuilder.hasErrors() ) {
            System.out.println( kbuilder.getErrors().toString() );
            throw new RuntimeException( "Unable to compile drl\"." );
        }
 
        // get the compiled packages (which are serializable)
        pkgs = kbuilder.getKnowledgePackages();
 
        // add the packages to a knowledgebase (deploy the knowledge packages).
        kbase.addKnowledgePackages( pkgs );
 
        ksession = kbase.newStatefulKnowledgeSession();
    }
 
    private static void fireRules(){
        ksession.fireAllRules();
    }
 
    private static void initMessageObject() {
        Message msg = new Message();
        msg.setType("Test");
        ksession.insert(msg);
    }
    
    public static void addDWC() {
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.YEAR, 2012);
	    cal.set(Calendar.MONTH, 3);
	    cal.set(Calendar.DAY_OF_MONTH, 30);
	    Date dateRep = cal.getTime();
		try {
			Occurrence occ = new Occurrence("1", "2012-03-27", "16:42:20", "1", "MovingImage", "1");
			Identification id = new Identification(1, 2, dateRep, 3);
			ImageSet is = new ImageSet(1, 1, "/home/encima/pictures/test.jpg");
			Vector<ImageSet> vis = new Vector<ImageSet>();
			vis.add(is);
			DarwinCore dwc = new DarwinCore(occ, id, vis);
			ksession.insert(dwc);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}