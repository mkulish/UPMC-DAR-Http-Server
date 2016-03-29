#include(chat/common/base-template)
    #override(style)
        #auth_table {
            width: 100%
        }

        #auth_table td {
            width: 50%
        }
    #end

    #override(header)
        #include(chat/common/base-header) #end

        <!--<script src="http://crypto-js.googlecode.com/svn/tags/3.0.2/build/rollups/md5.js"></script>-->
    #end

    #override(content)
        <script>
            function submitLogin(signup){
                var login = document.getElementById((signup === true) ? "signupLogin" : "login").value;
                var passhash = document.getElementById((signup === true) ? "signupPassword" : "password").value;
                //var passhash = CryptoJS.MD5(document.getElementById((signup === true) ? "signupPassword" : "password").value).toString();

                post((signup === true) ? '/chat/signup.do' : '/chat/login.do', {login: login, passhash: passhash});
            }
        </script>

        #if(model.get("loginFailed")!=null)
            <span><font color="red">The user with specified credentials is not found</font></span>
        #end
        #if(model.get("signupUserExists")!=null)
            <span><font color="red">This login is already used</font></span>
        #end

        <table id="auth_table">
        <tr>
            <td>
                <!-- login --!>
                <span><h2>Login</h2></span>

                <table>
                <tr>
                    <td><label for="login">Login</label></td>
                    <td><input id="login"/></td>
                </tr>
                <tr>
                    <td><label for="password">Password</label></td>
                    <td><input type="password" id="password"/></td>
                </tr>
                </table>
                <button onclick="submitLogin(false);">Login</button>
            </td>
            <td>
                <!-- sign up --!>
                <span><h2>Sign up</h2></span>

                <table>
                <tr>
                    <td><label for="signupLogin">Login</label></td>
                    <td><input id="signupLogin"/></td>
                </tr>
                <tr>
                    <td><label for="signupPassword">Password</label></td>
                    <td><input type="password" id="signupPassword"/></td>
                </tr>
                </table>
                <button onclick="submitLogin(true);">Sign up</button>
            </td>
        </tr>
        </table>
    #end

    #override(footer)
        #include(chat/common/base-footer) #end
    #end
#end