
$(document).ready(function() {

	$(document).on('mouseenter', "td , input[type='text'], select, .label_hide",  function () {
		var $this = $(this);	
		 
		if (this.offsetWidth < this.scrollWidth && $this.context) {
		$this.attr('title', $this.context);	
		}else if(this.offsetWidth < this.scrollWidth && $this.val()){
			$this.attr('title', $this.val());
		}

	if(this.offsetWidth < this.scrollWidth && !$this.attr('title')) {
        $this.attr('title', $this.text());
    }


   jQuery(".remove_td_title td").removeAttr("title");

		
	});
	
});
