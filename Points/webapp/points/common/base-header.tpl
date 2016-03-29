#import(edu.upmc.dar.server.security.entity.User)

<div id="auth" align="right">
    #if(! request.isSessionExpired())
        #{User user = (User) request.getSession().getAttribute("user");}

        #if(user == null)
            <span>Hello, Guest</span> | <span><a href="/points/login">login</a></span>
        #else
            <span>Hello, #[user.getLogin()]</span> | <span><a href=\"/logout\">logout</a></span>
        #end
    #else
        <span>The session has expired, please <a href="/login">login</a></span>
    #end
</div>