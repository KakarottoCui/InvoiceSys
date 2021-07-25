$(function(){
	var width = window.innerWidth-242+"px";
	$("#home").css('width', width);
	var h = $("#nav").height();
	var height = window.innerHeight-h-2+"px";
	$("#home").css('height', height);
	$("#dhl1").css('height', height);
	
	$("#dhl").click(function(){
		if($(this).hasClass('closed')){
			$("#dhl1").animate({'margin-left' : '0px'});
			$(this).removeClass('closed');
			var obj = window.innerWidth-242+"px";
			$("#home").animate({'margin-left' : '241px', 'width': obj});
		}else{
			$(this).addClass('closed');
			$("#dhl1").animate({'margin-left' : '-241px'});
			var obj = window.innerWidth+"px";
			$("#home").animate({'margin-left' : '0px', 'width': obj});
		}
	});	
	
	$(window).resize(function(){
	    var width = window.innerWidth-242+"px";
		$("#home").css('width', width);
		var h = $("#nav").height();
		var height = window.innerHeight-h-2+"px";
		$("#home").css('height', height);
		$("#dhl1").css('height', height);
	});
});
