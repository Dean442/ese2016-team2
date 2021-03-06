package ch.unibe.ese.team1.test.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.controller.pojos.forms.PlaceAuctionForm;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceBidForm;
import ch.unibe.ese.team1.controller.service.AuctionService;
import ch.unibe.ese.team1.model.Auction;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
public class AuctionServiceTest {

	@Autowired
	private AuctionService auctionService;

	@Autowired
	private UserDao userDao;
	
	private PlaceAuctionForm placeAuctionForm = new PlaceAuctionForm();
	private ArrayList<String> filePaths = new ArrayList<String>();

	@Before
	public void placeAuctionInDataBase() {
		// Preparation
		placeAuctionForm.setCity("3018 - Bern");
		placeAuctionForm.setPreferences("Test preferences");
		placeAuctionForm.setRoomDescription("Test Room description");
		placeAuctionForm.setPrize(600);
		placeAuctionForm.setSquareFootage(50);
		placeAuctionForm.setTitle("title");
		placeAuctionForm.setStreet("Hauptstrasse 13");
		placeAuctionForm.setRoomType("Studio");
		placeAuctionForm.setMoveInDate("27-02-2015");
		placeAuctionForm.setEndDate("12-12-2014");
		placeAuctionForm.setEndTime("12:00");

		placeAuctionForm.setSmokers(true);
		placeAuctionForm.setAnimals(false);
		placeAuctionForm.setGarden(true);
		placeAuctionForm.setBalcony(false);
		placeAuctionForm.setCellar(true);
		placeAuctionForm.setFurnished(false);
		placeAuctionForm.setCable(false);
		placeAuctionForm.setGarage(true);
		placeAuctionForm.setInternet(false);
		
		filePaths.add("/img/test/ad1_1.jpg");
	}

	/**
	 * In order to test the saved auction, I need to get it back from the DB
	 * again, so these two methods need to be tested together, normally we want
	 * to test things isolated of course. Testing just the returned auction from
	 * saveFrom() wouldn't answer the question whether the auction has been
	 * saved correctly to the db.
	 * 
	 * @throws ParseException
	 */
	@Test
	public void saveFromAndGetById() throws ParseException {
		User testPersonAuction1 = createUser("testPersonAuction@1.ch", "password", "testPerson", "Auction1", Gender.MALE, "Normal");
		testPersonAuction1.setAboutMe("TestPersonAuction1");
		userDao.save(testPersonAuction1);

		auctionService.saveFrom(placeAuctionForm, filePaths, testPersonAuction1);
		
		Auction auction1 = new Auction();
		Iterable<Auction> ads = auctionService.getAllAds();
		Iterator<Auction> iterator = ads.iterator();

		while (iterator.hasNext()) {
			auction1 = iterator.next();
		}

		// Testing
		assertTrue(auction1.getSmokers());
		assertFalse(auction1.getAnimals());
		assertEquals("Bern", auction1.getCity());
		assertEquals(3018, auction1.getZipcode());
		assertEquals("Test preferences", auction1.getPreferences());
		assertEquals("Test Room description", auction1.getRoomDescription());
		assertEquals(600, auction1.getPrize());
		assertEquals(50, auction1.getSquareFootage());
		assertEquals("title", auction1.getTitle());
		assertEquals("Hauptstrasse 13", auction1.getStreet());
		assertEquals("Studio", auction1.getRoomType());
		assertEquals("12:00, 12.12.2014", auction1.getEndTime());

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date result = df.parse("2015-02-27");

		assertEquals(0, result.compareTo(auction1.getMoveInDate()));
	}
	
	@Test 
	public void testPlaceNewBid() {
		User testPersonAuction2 = createUser("testPersonAuction@2.ch", "password", "testPerson", "Auction2", Gender.MALE, "Normal");
		testPersonAuction2.setAboutMe("TestPersonAuction2");
		userDao.save(testPersonAuction2);

		auctionService.saveFrom(placeAuctionForm, filePaths, testPersonAuction2);
		
		Auction auction2 = new Auction();
		Iterable<Auction> ads = auctionService.getAllAds();
		Iterator<Auction> iterator = ads.iterator();

		while (iterator.hasNext()) {
			auction2 = iterator.next();
		}
		
		PlaceBidForm placeBidForm = new PlaceBidForm();
		int newPrize = 700;
		placeBidForm.setPrize(newPrize);
		
		String bidderName = "Test bidder";
		
		Auction auctionAfterNewBid = auctionService.saveBidPrize(placeBidForm, auction2.getId(), bidderName);
		
		assertEquals(newPrize, auctionAfterNewBid.getPrize());
		assertEquals(bidderName, auctionAfterNewBid.getBidderName());
		assertEquals(auction2.getId(), auctionAfterNewBid.getId());
		
		
	}
	

	private User createUser(String email, String password, String firstName, String lastName, Gender gender,
			String account) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		userRoles.add(role);
		user.setUserRoles(userRoles);
		user.setAccount(account);
		return user;
	}

}
