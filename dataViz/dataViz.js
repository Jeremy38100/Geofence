let tooltip = d3.select("#world").append("div")
    .attr("class", "tooltip")
    .style("opacity", 0);

let colorScale = d3.schemeCategory10;
let geofences;
let users;

d3.json("data.json", (data) => {
    window.data = data;
    let svgContainer = d3.select("body")
        .append("svg")
        .attrs({
            width: data.size,
            height: data.size
        });

    drawGeofences(svgContainer, data);

    d3.json("moves.json", (dataPositions) => {
        console.log(dataPositions);
        console.log(dataPositions.positions);
        let userSelect = d3.select("#usersSelect")
            .selectAll("div")
            .data(dataPositions.users)
            .enter()
                .append("div")
                .attr("class", "userSelect");
        userSelect.append("input")
            .attrs((user) => {
                return {
                    type: "checkbox",
                    checked: true,
                    id: "userSelect_" + user.userName
                }
            })
            .on("click", function (user) {
                d3.select("#" + user.userName)
                    .style("opacity", this.checked ? "1" : "0");
            });
        userSelect.append("label")
            .attr("for", (user) => {return "userSelect_" + user.userName;})
            .html((user) => {return user.userName;});
        users = svgContainer
            .append("g")
            .attr("class", "users")
                .selectAll("g")
                .data(dataPositions.users)
                .enter()
                    .append("g")
                        .attr("id", (user) => { return user.userName; })
                        .attr("stroke", (e,i) => {return colorScale[i];})
                        .attr("fill", (e,i) => {return colorScale[i];});

        let position = users
            .append("g")
            .attr("class", "positions")
                .selectAll("path")
                .data((user) => {return user.positions;})
                .enter()
                .append("path")
                    .attr("d", d3.symbol().size(40).type(d3.symbolCross))
                    .attr("transform", (position) => {
                        return "translate(" + position.x + "," + position.y + ")"; })
                    .attr("r", "2")
                    .attr("class", "position")
                    .attr('fill-opacity', 1)
                    .on("mouseover", function (position) {
                        console.log(position);
                        tooltip
                            .style("opacity", 1)
                            .html("(" + position.x + ", " + position.y + ")" + " " + position.timestamp + "s")
                            .style("left", (d3.event.pageX) + 10 + "px")
                            .style("top", (d3.event.pageY) + "px");
                    })
                    .on("mousemove", () => {
                        tooltip
                            .style("left", (d3.event.pageX) + 10 + "px")
                            .style("top", (d3.event.pageY) + "px");
                    })
                    .on("mouseout", () => {
                        tooltip
                            .style("opacity", 0);
                    });

        let travels = users
            .append("g")
            .attr("class", "travel")
                .selectAll("line")
                .append("g")
                .data((user) => {
                    let positionsTravel = [];
                    for(let i=0; i<(user.positions.length-1); i++) {
                        positionsTravel.push([user.positions[i],user.positions[i+1]]);
                    }
                    return positionsTravel;
                })
                .enter()
                .append("line")
                    .attr("x1", (positions => {return positions[0].x;}))
                    .attr("y1", (positions => {return positions[0].y;}))
                    .attr("x2", (positions => {return positions[1].x;}))
                    .attr("y2", (positions => {return positions[1].y;}))
                    .attr("stroke-width", 1.5);
    });
});

function drawGeofences(svgContainer, data) {
    return svgContainer.append("g")
        .attr("class", "geofences")
        .selectAll("circle")
        .data(data.geofences)
        .enter()
        .append("circle")
        .attrs((geofence) => {
            return {
                cx: geofence.x,
                cy: geofence.y,
                r: geofence.radius,
                class: "geofence",
                'fill-opacity': 0
            }
        })
        .styles({
            fill: "red",
            stroke: "red"
        })
        .on("mouseover", function (d) {
            console.log(d);
            d3.select(this)
                .attr('fill-opacity', 0.5);
            tooltip
                .style("opacity", 1)
                .html(d.name)
                .style("left", (d3.event.pageX) + "px")
                .style("top", (d3.event.pageY) + "px");
        })
        .on("mousemove", function (d) {
            tooltip
                .style("left", (d3.event.pageX) + 10 + "px")
                .style("top", (d3.event.pageY) + "px");
        })
        .on("mouseout", function (d) {
            console.log(d);
            d3.select(this)
                .attr('fill-opacity', 0);
            tooltip
                .style("opacity", 0);
        });
}

function switchShowGeofences(input) {
    geofences.each(function () {
        d3.select(this)
            .style("display", input.checked ? "inline" : "none");
    });
}