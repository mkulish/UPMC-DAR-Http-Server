#import(edu.upmc.dar.applications.chat.entity.ChatMember)

#include(chat/common/base-template)
    #override(header)
        #include(chat/common/base-header) #end

        <script type="text/javascript">
            #if(request.getSession().getAttribute("chatMember") != null)
            /*
            function openSSEConnection(){
                var eventSource = new EventSource("/chat/events.stream");
                eventSource.onmessage = function(event) {
                    alert(event.data);
                };
                eventSource.addEventListener('status', function(event) {
                    var data = JSON.parse(event.data);
                    updateStatus(data.id, data.status);
                }, false);
                eventSource.addEventListener('chatMessage', function(event) {
                    addMessage(event.data);
                }, false);
                eventSource.onerror = function(e) {
                    if (e.readyState != EventSource.CLOSED) {
                        alert("Error on establishing SSE connection");
                    }
                };
            }
            */
            #end

            var lastMessageId = -1;
            window.onload = function () {
                #if(request.getSession().getAttribute("chatMember") != null)
                document.getElementById("messageText").onkeydown = function (ev) {
                    var key;
                    var isShift;
                    if (window.event) {
                        key = window.event.keyCode;
                        isShift = !!window.event.shiftKey; // typecast to boolean
                    } else {
                        key = ev.which;
                        isShift = !!ev.shiftKey;
                    }
                    if (! isShift && key == 13) {
                        sendMessage();
                        document.getElementById("messageText").value = "";
                        return false;
                    } else {
                        return true;
                    }
                };
                #end
                loop();
            };

            function loop(){
                checkStatuses();
                checkMessages();
                setTimeout(loop, 1000);
            }

            function checkStatuses(){
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (xhttp.readyState == 4 && xhttp.status == 200) {
                        var data = JSON.parse(xhttp.responseText);
                        for(var i = 0; i < data.length; ++i){
                            updateStatus(data[i].id, data[i].status, data[i].login);
                        }
                    }
                };
                xhttp.open("GET", "/chat/statuses.data", true);
                xhttp.send();
            }

            function checkMessages(){
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (xhttp.readyState == 4 && xhttp.status == 200) {
                        var data = JSON.parse(xhttp.responseText);
                        for(var i = 0; i < data.length; ++i){
                            addMessage(data[i]);
                        }
                    }
                };
                xhttp.open("GET", "/chat/messages.data?last=" + lastMessageId, true);
                xhttp.send();
            }

            function updateStatus(id, status, name){
                var memberElem = document.getElementById('member-list-'+id);
                if(memberElem != null){
                    memberElem.className = 'status-' + status;
                }/* else {
                    var newMemberElem = document.createElement("li", 'member-list-' + id);
                    newMemberElem.className = "status-"+status;
                    newMemberElem.innerHTML = "<span>" + name + "</span>";
                    document.getElementById("members-list").appendChild(newMemberElem);
                }*/
            }
            function addMessage(message){
                if(message.id > lastMessageId){
                    lastMessageId = message.id;
                }

                var newMessageDiv = document.createElement("div", 'message-' + message.id);
                newMessageDiv.className = "message";
                newMessageDiv.innerHTML = '<span class="datetime">' + message.datetime + ', </span>';
                newMessageDiv.innerHTML+= '<span class="sender">' + message.sender.login + '</span>:<br/>';
                newMessageDiv.innerHTML+= '<span class="text">' + message.text + '</span>';

                document.getElementById('conversation').appendChild(newMessageDiv);
            }
            function sendMessage(){
                var text = document.getElementById("messageText").value;
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (xhttp.readyState == 4 && xhttp.status == 200) {
                        checkMessages();
                    }
                };
                xhttp.open("POST", "/chat/send-message.do", true);
                xhttp.send("text="+text);
            }
            function changeStatus(){
                var status = document.getElementById("statusSelect").selectedIndex;
                var xhttp = new XMLHttpRequest();
                xhttp.open("POST", "/chat/change-status.do", true);
                xhttp.send("status="+status);
            }
        </script>
    #end

    #override(content)
        <dic id="container">

            <div id="members" class="right">
                <span>All members:</span>
                <ul id="members-list">
                #for(ChatMember otherMember : model.<ChatMember>getCollection("members"))

                    <li class="status-#[otherMember.getStatus().ordinal()]" id="member-list-#[otherMember.getId()]">
                        <span>#[otherMember.getLogin()]</span>
                    </li>
                #end
                </ul>
            </div>

            <div id="chat">
                <div id="conversation">

                </div>
                #{ChatMember member = (ChatMember)request.getSession().getAttribute("chatMember");}
                #if(member != null)
                    <div id="message">
                        <table style="width: 100%;">
                        <tr>
                            <td style="width: 150px;">
                                <div align="center">
                                <select id="statusSelect" onchange="changeStatus();">
                                    <option><font color="green">Available</font></option>
                                    <option><font color="yellow">Absent</font></option>
                                    <option><font color="red">Do not disturb</font></option>
                                    <option><font color="black">Offline</font></option>
                                </select>
                                </div>
                            </td>
                            <td>
                                 <textarea id="messageText" placeholder="Type your message here..."></textarea>
                            </td>
                        </tr>
                        </table>
                    </div>
                #end
            </div>
        </div>
    #end

    #override(style)
        #container {
            width: 100%;
            height: 100%;
            float: left;
            margin-right: -310px;
        }
        #message {
            height: 50px;
            margin-right: 310px;
            position: absolute;
            bottom: 0;
            right: 0;
            left: 0;
            padding: 10px;
        }
        #message textarea {
            width: 100%;
            max-height: 40px;
        }
        #conversation {
            position: absolute;
            top: 0px;
            left: 0;
            right: 0;
            bottom: 0px;
            margin-top: 10px;
            margin-left: 10px;
            margin-right: 320px;
            margin-bottom: 80px;
            border-style: solid;
            border-width: 1px;
            border-color: grey;
        }
        #chat {
            margin-right: 310px;
            height: 100%;
            /*border-style: solid;
            border-width: 1px;*/
        }
        #members {
            width: 300px;
            height: 100%;
            border-style: solid;
            border-width: 1px;
            border-color: grey;
        }

        .datetime {
            font-style: italic;
        }
        .sender {
            font-weight: bold;
            font-size: 16pt;
        }
        .text {
            font-size: 18pt;
            padding-top: 5px;
        }
        ul {
            list-style: none;
            padding:0;
            margin:0;
        }
        li {
            padding-left: 1em;
            text-indent: -.7em;
            font-size: 16pt;
        }
        li:before {
            content: "• ";
            font-weight: bold;
        }
        li.status-0:before {color: green;}
        li.status-1:before {color: yellow;}
        li.status-2:before {color: red;}
        li.status-3:before {color: black;}
    #end

    #override(footer)
        #include(chat/common/base-footer) #end
    #end
#end