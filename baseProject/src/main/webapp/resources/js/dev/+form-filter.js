// Should already exist.
var flatfindr = flatfindr || {};



/**
 * pseudo namespace for docs
 * @param  {object} window
 * @param  {object} document the document element
 * @param  {object} $        jQuery
 * @param  {object} jsp      globals passed in jsp
 * @namespace
 * @memberOf flatfindr
 */
flatfindr.filter = function (window, document, $, jsp) {

  var
    /**
     * The base duration in ms that corresponds to the one set in _vars.scss
     *
     * @type {Number}
     * @constant
     */
    BASE_DURATION = 350,



    /**
     * An opinionated bit of an extra delay. (usability specific)
     *
     * @type {Number}
     * @constant
     */
    DURATION_BUFFER = 50,



    /**
     *
     * @type {object}
     */
    $form_filter = $('.form-filter form'),



    /**
     * The scrollable container.
     *
     * @type {object}
     */
    $container_scroll = $('.form-filter .container-scroll'),





    /**
    * All input fields within the search form scrollable container.
    */
    $input_fields =
      $container_scroll
        .find('input')
        .focus(alignInputToTop);




  /**
   * All checkboxes within the search form scrollable container.
   */
  $container_scroll.find('label').on('click', alignInputToTop);



  /**
   * The clear button.
   */
  $form_filter.find('button[type=reset]').on('click', reset);




  /**
   *
   */
  // $('.js-new-search').on('click', function() {
  //   $form_filter[0].reset();
  //   $input_fields.val(null).attr('checked', false);
  //   $('#city')
  //     .focus()
  //     .attr('placeholder', 'City / ZIP');
  // });


  /**
   * Align the focused or cliced element to top of the visible part of the
   * scrollable $container_scroll.
   * @param  {object} e the event object click or touch.
   */
  function alignInputToTop() {
    var
      $this = $(this),
      offset_mod = 0;

    if ($this.is('.js-has-label')) {
      offset_mod =
        $this
          .parents('.row')
          .first()
          .find('label')
          .outerHeight();
    }

    animateScrollTop(
      $container_scroll.scrollTop() + $(this).position().top - offset_mod
    );
  }



  /**
   * Reset from and align $container_scroll to top, so scrollTop position is 0.
   */
  function reset() {
    animateScrollTop(0);
  }



  /**
   * Animate the change of the scrollTop position of the scrollable
   * element $container_scroll.
   * @param  {number} scrollTop the new scroll position to be animated to.
   */
  function animateScrollTop(scrollTop) {
    $container_scroll
      .delay(DURATION_BUFFER)
      .animate({scrollTop: scrollTop}, BASE_DURATION);
  }



  // @Jerome
  // TODO: Clean the closet !!
  // ==========================================================================
  $("#city").autocomplete({
    minLength : 2,
    enabled : true,
    autoFocus : true,
    source : jsp.zipcodes
  });

  // @Jerome
  // TODO: Do this in a beforeSubmit handler.
  // var price = document.getElementById('prizeInput');
  // var radius = document.getElementById('radiusInput');
  // if(price.value == null || price.value == "" || price.value == "0")
  //   price.value = "500";
  // if(radius.value == null || radius.value == "" || radius.value == "0")
  //   radius.value = "5";
  //

  $('.js-has-label').hide(0);

  $("#earliestMoveInDate").datepicker({
    altField: '#field-earliestMoveInDate',
    dateFormat: 'dd-mm-yy'
  });
  $("#latestMoveInDate").datepicker({
    altField: '#field-latestMoveInDate',
    dateFormat : 'dd-mm-yy'
  });
  $("#earliestMoveOutDate").datepicker({
    altField: '#field-earliestMoveOutDate',
    dateFormat : 'dd-mm-yy'
  });
  $("#latestMoveOutDate").datepicker({
    altField: '#field-latestMoveOutDate',
    dateFormat : 'dd-mm-yy'
  });


  function validateType_FilterForm(form)
  {
  	var room = document.getElementById('room');
  	var studio = document.getElementById('studio');
  	var house = document.getElementById('house');
  	var neither = document.getElementById('neither');
  	var filtered = document.getElementById('filtered');

  	neither.checked = false;
  	if(!room.checked && !studio.checked && !house.checked) {
  		neither.checked = true;
  	}
  	filtered.checked = true;
  }


  $('[type=submit]').click(function () {
    validateType_FilterForm($(this)[0]);
  });



  $('#modus').on('change', sort_div_attribute);

  /*
   * This script takes all the resultAd divs and sorts them by a parameter specified by the user.
   * No arguments need to be passed, since the function simply looks up the dropdown selection.
   */
  function sort_div_attribute() {
      //determine sort modus (by which attribute, asc/desc)
      var sortmode = $('#modus').find(":selected").val();

      //only start the process if a modus has been selected
      if(sortmode.length > 0) {
      	var attname;

      	//determine which variable we pass to the sort function
  		if(sortmode == "price_asc" || sortmode == "price_desc")
  			attname = 'data-price';
  	    else if(sortmode == "moveIn_asc" || sortmode == "moveIn_desc")
  			attname = 'data-moveIn';
  	    else
  			attname = 'data-age';

  		//copying divs into an array which we're going to sort
  	    var divsbucket = new Array();
  	    var divslist = $('li.resultAd');
  	    var divlength = divslist.length;
  	    for (a = 0; a < divlength; a++) {
  			divsbucket[a] = new Array();
  			divsbucket[a][0] = divslist[a].getAttribute(attname);
  			divsbucket[a][1] = divslist[a];
  			divslist[a].remove();
  	    }

  	    //sort the array
  		divsbucket.sort(function(a, b) {
  	    if (a[0] == b[0])
  			return 0;
  	    else if (a[0] > b[0])
  			return 1;
          else
  			return -1;
  		});

  	    //invert sorted array for certain sort options
  		if(sortmode == "price_desc" || sortmode == "moveIn_asc" || sortmode == "dateAge_asc")
  			divsbucket.reverse();

  	    //insert sorted divs into document again
  		for(a = 0; a < divlength; a++)
        $("#resultsDiv").append($(divsbucket[a][1]));
  	}
  }


  /**
   * Yeah, jquery ui... blah blah
   */
  setTimeout(function() {
    $form_filter[0].reset();
    $('.js-has-label').fadeIn(BASE_DURATION);
  }, BASE_DURATION);


  // ==========================================================================

};