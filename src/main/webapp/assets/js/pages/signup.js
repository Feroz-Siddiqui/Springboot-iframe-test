$( document ).ready(function() {
    console.log( "ready!" );
    
    $("div#profile_dropzone").dropzone({ url: "/file/uploadFile" });
});