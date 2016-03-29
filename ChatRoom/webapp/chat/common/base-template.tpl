<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="UTF-8">
        <style type="text/css">
            .right {
                float:right;
            }

            #header {
                height: 20px;
                left: 0px;
                right: 0px;
            }
            #content {
                position: absolute;
                top: 50px;
                bottom: 50px;
                left: 10px;
                right: 10px;
            }
            #footer {
                height: 50px;
                position: absolute;
                bottom: 0px;
                left: 0px;
                right: 0px;
            }
            #disclaimer {
                font-weight: bold;
                font-size: 14pt;
                text-align: center;
            }
            #define(style)
        </style>

        <script type="application/javascript">
            function post(path, params, method) {
                method = method || "post"; // Set method to post by default if not specified.

                // The rest of this code assumes you are not using a library.
                // It can be made less wordy if you use one.
                var form = document.createElement("form");
                form.setAttribute("method", method);
                form.setAttribute("action", path);

                if(params){
                    for(var key in params) {
                        if(params.hasOwnProperty(key)) {
                            var hiddenField = document.createElement("input");
                            hiddenField.setAttribute("type", "hidden");
                            hiddenField.setAttribute("name", key);
                            hiddenField.setAttribute("value", params[key]);

                            form.appendChild(hiddenField);
                         }
                    }
                }

                document.body.appendChild(form);
                form.submit();
                document.body.removeChild(form);
            }
        </script>
    </head>

    <body>
        <div id="header">
            #define(header)
        </div>
        <hr/>

        <div id="content">
            #define(content)
        </div>

        <div id="footer">
            #define(footer)
        </div>
    </body>
</html>