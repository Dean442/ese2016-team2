package ch.unibe.ese.team1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team1.controller.service.AdService;

/**
 * @Jerome
 * Add these imports for basic search functionality.
 */
import org.springframework.web.bind.annotation.ModelAttribute;
import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;

/**
 * This controller handles request concerning the home page and several other
 * simple pages.
 */
@Controller
public class IndexController {

	@Autowired
	private AdService adService;

	/**
	 * @Jerome
	 * Add these properties for basic search functionality.
	 */
	private SearchForm searchForm;


	/** Displays the home page. */
	@RequestMapping(value = "/")
	public ModelAndView index() {
		ModelAndView model = new ModelAndView("index");
		model.addObject("newestAds", adService.getNewestAds(4));
		return model;
	}

	/** Displays the about us page. */
	@RequestMapping(value = "/about")
	public ModelAndView about() {
		return new ModelAndView("about");
	}

	/** Displays the disclaimer page. */
	@RequestMapping(value = "/disclaimer")
	public ModelAndView disclaimer() {
		return new ModelAndView("disclaimer");
	}

	/**
	 * @Jerome
	 * This mapping will be just temporarily used for developing the new
	 * front-end within the files with a tilde-prefix, e.g. ~index.jsp, which are
	 * not linked within the public pages. They are only reachable by typing
	 * the provided values into the address bar. E.g. for the ~index page, this
	 * would be ~.
	 */
	@RequestMapping(value = "/~")
	public ModelAndView _index() {
		ModelAndView model = new ModelAndView("~index");
		model.addObject("newestAds", adService.getNewestAds(4));
		return model;
	}


	/**
	 * @Jerome
	 * Add this attribute for basic search functionality.
	 */
	@ModelAttribute
	public SearchForm getSearchForm() {
		if (searchForm == null) {
			searchForm = new SearchForm();
		}
		return searchForm;
	}
}
