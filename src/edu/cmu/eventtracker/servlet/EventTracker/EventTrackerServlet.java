package edu.cmu.eventtracker.servlet.EventTracker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.caucho.hessian.client.HessianProxyFactory;

import edu.cmu.eventtracker.action.PingAction;
import edu.cmu.eventtracker.dto.Event;
import edu.cmu.eventtracker.dto.PingResponse;
import edu.cmu.eventtracker.geoserver.GeoService;
import edu.cmu.eventtracker.geoserver.GeoServiceFacade;
import edu.cmu.eventtracker.serverlocator.ServerLocator;
import edu.cmu.eventtracker.serverlocator.ServerLocatorService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
/**
 * Servlet implementation class EventTrackerServlet
 */
public class EventTrackerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String rootElementName = "list";
	public final static String markerElementName = "marker";
	public final static String markerName  = "name";
	public final static String markerId = "id";
	public final static String markerLon = "longitude";
	public final static String markerLat = "latitude";
	public final static String xmlDeclaration = "<?xml version=\"1.0\"?>";
	private HessianProxyFactory factory;
	private InetAddress addr;



    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventTrackerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		HessianProxyFactory factory = new HessianProxyFactory();
		addr = InetAddress.getLocalHost();

		ServerLocatorService locatorService = getServiceLocator();
		
		String username = request.getParameter("username");
		String lat = request.getParameter("lat");
		String lng = request.getParameter("lon");
		
		//check if null?
		Double latD = Double.valueOf(lat);
		Double lngD = Double.valueOf(lng);

		
		PingResponse res = new GeoServiceFacade(
				locatorService.getLocationShard(latD, lngD))
				.execute(new PingAction(latD, lngD, username));
		
		Event[] events;
		//events = res.getEvents();
		
		Gson gson = new GsonBuilder().create();
		gson.toJson(res, Event[].class);
		
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	private ServerLocatorService getServiceLocator()
			throws MalformedURLException {
		
		String locatorURL = ServerLocator.getURL(addr.getHostName(),
				ServerLocator.SERVER_LOCATOR_PORT);
		return (ServerLocatorService) factory.create(
				ServerLocatorService.class, locatorURL);
	}

}
