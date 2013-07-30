package com.sample;
 
import java.io.Reader;
import java.io.StringReader;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
 
public class DroolsTest {
 
    private static KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    private static Collection<KnowledgePackage> pkgs;
    private static KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
    private static StatefulKnowledgeSession ksession;
 
    public static void main(final String[] args) {
 
        initDrools();
        initMessageObject();
        fireRules();
 
    }
 
    private static void initDrools(){
 
    	try {
			Class.forName("com.mysql.jdbc.Driver");
			String connString = "jdbc:mysql://localhost:3306/drools";
			Connection conn = DriverManager.getConnection(connString, "root", null);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM rules");
			while(rs.next()) {
				String rule = rs.getString("rule");
				System.out.println(rule);
				Resource myResource = ResourceFactory.newReaderResource((Reader) new StringReader(rule));
		        kbuilder.add(myResource, ResourceType.DRL);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
}