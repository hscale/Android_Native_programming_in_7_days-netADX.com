var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");
var mangUsername =[];

server.listen(process.env.PORT || 3000);
/*
app.get("/", function(req, res){
	res.sendFile(__dirname + "/index.html");	
});
*/

io.sockets.on('connection', function (socket) {

    console.log("Co nguoi connect ne");
    socket.on("client-gui-username", function (data) {

        var kq=false;

        if (mangUsername.indexOf(data) > -1){
            console.log("da ton tai user "+data);
            socket.emit("foo","da ton tai");
        }else{
            mangUsername.push(data);
            console.log("client vua dang ky:"+data);
            kq=true;
            // emit toi tat ca moi nguoi
            io.sockets.emit('serverguinguoimoi', { nguoimoi: data });

        }

         // emit tới máy nguoi vừa gửi
         	socket.emit('kqdangky', { noidung: kq });
        /*console.log(mangUsername);*/
    });





});
