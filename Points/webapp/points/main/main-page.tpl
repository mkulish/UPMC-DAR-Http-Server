#import(edu.upmc.dar.applications.points.entity.Point2D)

#include(points/common/base-template)
    #override(style)
        ##pointsTable{
            font-size: 16;
        }
        ##pointsTable td {
            min-width: 100px;
        }
        ##pointsTable tr.header {
            height: 40px;
            font-weight: bold;
            font-size: 20;
        }
    #end

    #override(header)
        #include(points/common/base-header) #end
    #end

    #override(content)
        <span><h3>Currently available points:</h3></span>

        #{List<Point2D> points = (List<Point2D>)model.get("points");}

        #if(points != null && ! points.isEmpty())
            <table id="pointsTable">

            <tr class="header">
                <td>ID</td>
                <td>X</td>
                <td>Y</td>
            </tr>

            #for(Point2D point : points)
                <tr>
                    <td>#[point.getId()]</td>
                    <td>#[point.getX()]</td>
                    <td>#[point.getY()]</td>
                </tr>
            #end

            </table>
        #else
            <span><i>No points found</i></span>
        #end
    #end

    #override(footer)
        #include(points/common/base-footer) #end
    #end
#end