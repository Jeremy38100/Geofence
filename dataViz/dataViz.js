let tooltip = d3.select("#world").append("div")
    .attr("class", "tooltip")
    .style("opacity", 0);

let circles;
let users;

d3.json("data.json", (data) => {
    window.data = data;
    let svgContainer = d3.select("body")
        .append("svg")
            .attr("width", data.size)
            .attr("height", data.size);

    circles = svgContainer
        .append("g")
            .attr("class", "geofences")
            .selectAll("circle")
            .data(data.geofences)
            .enter()
                .append("circle")
                    .attr("cx", (d) => { return d.x; })
                    .attr("cy", (d) => { return d.y; })
                    .attr("r", (d) => { return d.radius; })
                    .attr("class", "geofence")
                    .style("fill", "red")
                    .attr('fill-opacity', 0)
                    .style("stroke", "red")
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
    d3.json("moves.json", (dataPositions) => {
        console.log(dataPositions);
        console.log(dataPositions.positions);
        users = svgContainer
            .append("g")
            .attr("class", "positions")
                .selectAll("g")
                .data(dataPositions.users)
                .enter()
                    .append("g")
                        .attr("class", (user) => { return "group_" + user.userName; });
        users.selectAll("path")
            .append("g")
            .data((user) => {return user.positions;})
            .enter()
            .append("path")
                .attr("d", d3.symbol().size(40).type(d3.symbolCross))
                .attr("transform", (position) => {
                    return "translate(" + position.x + "," + position.y + ")"; })
                .attr("r", "2")
                .attr("class", "position")
                .style("fill", "grey")
                .attr('fill-opacity', 1)
                .on("mouseover", function (position) {
                    console.log(position);
                    tooltip
                        .style("opacity", 1)
                        .html(position.username)
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
    });
});



function switchShowGeofences(input) {
    circles.each(function () {
        d3.select(this)
            .style("display", input.checked ? "inline" : "none");
    });
}
function switchShowPositions(input) {
    users.each(function () {
        d3.select(this)
            .style("display", input.checked ? "inline" : "none");
    });
}