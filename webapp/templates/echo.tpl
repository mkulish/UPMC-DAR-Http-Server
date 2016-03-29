<!DOCTYPE HTML>
<html>
    <head>
        <style>
            #mainTable{
                font-size: 16;
            }
            #mainTable td {
                min-width: 400px;
            }
            #mainTable tr.header {
                height: 40px;
                font-weight: bold;
                font-size: 20;
            }
        </style>
    </head>

    <body>
        <span><h1>Received HTTP request data</h1></span>

        <table id="mainTable">
            <tr><td colspan="2"><h3> </h3></td></tr>

            <tr>
                <td><i>Method</i></td>
                <td>#[request.getMethod()]</td>
            </tr>

            <tr>
                <td><i>URL</i></td>
                <td>#[request.getUrl()]</td>
            </tr>

            <tr>
                <td><i>Version</i></td>
                <td>#[request.getVersion()]</td>
            </tr>

            <tr>
                <td colspan="2"><h3>Header</h3></td>
            </tr>
            #for(Map.Entry<String, String> param : request.getHeader().getParamsMap().entrySet())
                <tr>
                    <td><i>#[param.getKey()]</i></td>
                    <td>#[param.getValue()]</td>
                </tr>
            #end

            #if(request.getBody() != null)
                <tr><td colspan=\"2\"><h3>Request body</h3></td></tr>\n");
                <tr><td colspan=\"2\">#[request.getBody()]</td></tr>\n");
            #end

        </table>
    </body>
</html>