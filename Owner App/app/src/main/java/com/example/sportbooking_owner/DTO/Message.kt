package com.example.sportbooking_owner.DTO

class Message {
    var message:String?=null
    var senderId:String?=null
    constructor(){}
    constructor(m:String,sId:String){
        message=m;
        senderId=sId
    }
}