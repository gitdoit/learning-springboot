// 开始训练，初始化
var send_init = "/app/object";
// 发送录音流
var send_audio = "/app/audioStream";
// 接收录音文件
var get_audio_byte = "/user/audio/stream";
// 接收录音文本翻译
var get_audio_text = "/user/audio/text";

//API https://github.com/JSteunou/webstomp-client
var gRecorder = null;
var timOutId;
SRecorder.get(function (rec) {
    gRecorder = rec;
});
var start =document.getElementById("b");
var end =document.getElementById("c");
// webstomp.client('ws://localhost:8080/socket',{binary:true});

var stompClient =  webstomp.client('ws://localhost/socket');

// 向服务器发起Stomp链接
stompClient.connect({}, function () {
    stompClient.send(send_init,JSON.stringify({robotId:1}),{});


    // 订阅录音流
    stompClient.subscribe(get_audio_byte,function (msg) {
        console.log("收到音频流");
        // 处理音频流
        if(msg.body.constructor == String){
            console.log("--------=====------")
        }
        processAudioStream(msg.body)
    });
    // 订阅录音文本翻译
    stompClient.subscribe(get_audio_text,function (msg) {
        // 处理录音文本翻译:机器人、客户
        processAudioText(msg)
    });

},function () {console.warn("stomp连接失败！")});



// 开始发送录音
start.onclick = function () {
    // 初始化训练命令
    stompClient.send(send_init,"123456",{});
    // 延时0.5秒开始录音
    window.setTimeout(function () {gRecorder.run();},500);
    // 每3秒发送一次录音
    timOutId = window.setInterval(function () {
        // Base64 编码
        var reader = new FileReader();
        reader.onload = function (e) {
            // destination, body, headers
            stompClient.send(send_audio,reader.result,{});
            console.log("音频数据已发送！");
        };
        reader.readAsDataURL(gRecorder.getAndClear());
    }, 3000);
};
// 结束训练
end.onclick = function (ev) {
    gRecorder.stop();
    stompClient.close();
    door = false;
    clearInterval(timOutId);
}

// 处理音频流，分块接收整段音频 前端连续播放是一件
var processAudioStream = function (stream) {
    // Base64 -> ArrayBuffer
    var  byteArray = Base64Binary.decodeArrayBuffer(stream);
    // 播放
    playAudio(byteArray);
};


/**
 * 处理音频翻译
 * @param text
 */
var processAudioText = function (text) {
    console.log(text)
    // 己方翻译  对方翻译
};




/**
 * 播放音频
 * @param arrayBuffer
 */
var playAudio = function (arrayBuffer) {
    var source = context.createBufferSource();
    context.decodeAudioData(arrayBuffer,function (buffer) {
        //  告诉音频源 播放哪一段音频
        source.buffer = buffer;
        // 连接到输出源
        source.connect(context.destination);
        //开始播放
        source.start(0);
    });
}

var Base64Binary = {
    _keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

    /* will return a  Uint8Array type */
    decodeArrayBuffer: function(input) {
        var bytes = (input.length/4) * 3;
        var ab = new ArrayBuffer(bytes);
        this.decode(input, ab);

        return ab;
    },

    removePaddingChars: function(input){
        var lkey = this._keyStr.indexOf(input.charAt(input.length - 1));
        if(lkey == 64){
            return input.substring(0,input.length - 1);
        }
        return input;
    },

    decode: function (input, arrayBuffer) {
        //get last chars to see if are valid
        input = this.removePaddingChars(input);
        input = this.removePaddingChars(input);

        var bytes = parseInt((input.length / 4) * 3, 10);

        var uarray;
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;
        var j = 0;

        if (arrayBuffer)
            uarray = new Uint8Array(arrayBuffer);
        else
            uarray = new Uint8Array(bytes);

        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

        for (i=0; i<bytes; i+=3) {
            //get the 3 octects in 4 ascii chars
            enc1 = this._keyStr.indexOf(input.charAt(j++));
            enc2 = this._keyStr.indexOf(input.charAt(j++));
            enc3 = this._keyStr.indexOf(input.charAt(j++));
            enc4 = this._keyStr.indexOf(input.charAt(j++));

            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;

            uarray[i] = chr1;
            if (enc3 != 64) uarray[i+1] = chr2;
            if (enc4 != 64) uarray[i+2] = chr3;
        }

        return uarray;
    }
}