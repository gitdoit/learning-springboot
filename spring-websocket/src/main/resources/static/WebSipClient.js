var WebSipClient = function(server,option) {
	JsSIP.C.SESSION_EXPIRES=120;
    JsSIP.C.MIN_SESSION_EXPIRES=120;
	

	var socket = new JsSIP.WebSocketInterface(server);
    // sip:1002@192.168.0.31
	var configuration = {
        sockets: [socket],
        uri: 'sip:'+option.user+'@'+option.domain,
        realm: "192.168.0.31",
        ha1: option.ha1,
        register: false
    };


    this.oSipAudio = document.createElement("audio");

    this.session = undefined;

    this.ai = 'sip:'+option.aiNumber+"@"+option.domain;

    var coolPhone = new JsSIP.UA(configuration);
    coolPhone.on('connected', function (e) { /* Your code here */
    });

    coolPhone.on('disconnected', function (e) { /* Your code here */
    });

    coolPhone.on('newRTCSession', function (e) { /* Your code here */
    });

    coolPhone.on('registered', function (e) { /* Your code here */
    });
    coolPhone.on('unregistered', function (e) { /* Your code here */
    });
    coolPhone.on('registrationFailed', function (e) { /* Your code here */
    });

    coolPhone.start();

    this.ua = coolPhone;

    //TODO complete logic
    this.eventHandlers = {
            'progress': function (e) {
                console.log('call is in progress');
            },
            'failed': function (e) {
                console.log('call failed with cause: ' + e.cause);
            },
            'ended': function (e) {
                console.log('call ended with cause: ' + e.cause);
            },
            'confirmed': function (e) {
                console.log('call confirmed');
            },
            'peerconnection':function(e){
                console.log('call connection');
            }
    };

};



WebSipClient.prototype.callAi = function(robotId) {
	var options = {
            'eventHandlers': this.eventHandlers,
            'mediaConstraints': {'audio': true, 'video': false},
            'extraHeaders':[
                "X-robotId: "+robotId
            ]
        };

        var ins = this;

        var session = this.ua.call(this.ai, options);

        session.connection.addEventListener('addstream', function(event) {
            //TODO clean log
            console.log(event);
            ins.oSipAudio.srcObject = event.stream;
            ins.oSipAudio.play();
        });

        this.session = session;
};



WebSipClient.prototype.hangup = function() {
	this.session&&this.session.terminate();
	this.session = undefined;
};














