$(function(){
    $("#newsi-form").validate({
       
       
    });
    
    $("#form-total").steps({
        headerTag: "h3",
        bodyTag: "fieldset",
        transitionEffect: "fade",
        // enableAllSteps: true,
        autoFocus: true,
        transitionEffectSpeed: 500,
        titleTemplate : '<h3 class="title">#title#</h3>',
        labels: {
            previous :'Prev',
            next : 'Next',
            finish : 'Submit',
            current : ''
        },

        onFinished: function (event, currentIndex)
        {
            alert('Submited');
        }
     
    });
});



