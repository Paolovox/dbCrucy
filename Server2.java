import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import it.uniroma1.lcl.cruciverba.Cruciverba;
import it.uniroma1.lcl.cruciverba.CruciverbaBuilder;
import it.uniroma1.lcl.cruciverba.CruciverbaBuilderException;

/**
 * Servlet implementation class Server
 */
@WebServlet("/index.html")
public class Server2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Server2() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private String path;
    
    public void init()
    {
    	
    	this.path = this.getServletContext().getRealPath("vocabIT");
    	System.out.println(path.toString());
			File f = new File(path.toString());
			System.out.println("ESISTE = "+f.exists());
		
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int altezza = Integer.parseInt(request.getParameter("altezza"));
		int larghezza = Integer.parseInt(request.getParameter("larghezza"));
	//	response.setContentType("application/x-java-serialized-object");
		
		//response.getWriter().println("ciao "+parametro);

		int max = Math.max(larghezza, altezza);
		System.out.println("h="+altezza+" l="+larghezza);
		
		Map<String, String> definitions = new HashMap<String, String>();
		Files.newBufferedReader(Paths.get(path)).lines()
				.forEach((String l) -> {
					String w[] = l.split("\t");
					if (w[0].length() <= max)
						definitions.put(w[0], w[1]);
				});  
		
		try {
			Cruciverba c = new CruciverbaBuilder()
							.setParole(definitions)
							.setAltezza(altezza)
							.setLarghezza(larghezza)
							.build();
			
			File file = new File("file.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Cruciverba.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(c, file);
			jaxbMarshaller.marshal(c, System.out);
			
			

		} catch (CruciverbaBuilderException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		System.out.println(new Date());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}	//http://93.88.106.32:8080/myWeb/
	//http://93.88.106.32:8080/Crucy/?param1=Ceuza
