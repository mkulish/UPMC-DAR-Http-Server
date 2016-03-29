#import(edu.upmc.dar.server.security.entity.User)

<div id="auth" class="right">
    #if(! request.isSessionExpired())
        #{User user = (User) request.getSession().getAttribute("user");}

        #if(user == null)
            <span>Hello, Guest</span> | <span><a href="/chat/login">login</a></span>
        #else
            <span>Hello, #[user.getLogin()]</span> | <span><a href="#" onclick="post('/chat/logout.do');">logout</a></span>
        #end
    #else
        <span>The session has expired, please <a href="/chat/login">login</a></span>
    #end
</div>
<div id="disclaimer">
    <span>Welcome to the chat room!</span>
</div>