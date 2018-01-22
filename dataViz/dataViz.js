d3.json("data.json", (data) => {
    window.data = data;
    let svgContainer = d3.select("body")
        .append("svg")
            .attr("width", data.size)
            .attr("height", data.size);

    let circles = svgContainer.selectAll("circle")
        .data(data.geofences)
        .enter()
            .append("circle")
                .attr("cx", (d) => { return d.x; })
                .attr("cy", (d) => { return d.y; })
                .attr("r", (d) => { return d.radius; })
                .style("fill", "red")
                .attr('fill-opacity', 0)
                .style("stroke", "red")
                .on("mouseover", function (d) {
                    console.log(d);
                    d3.select(this)
                        .attr('fill-opacity', 0.5);
                })
                .on("mouseout", function (d) {
                    console.log(d);
                    d3.select(this)
                        .attr('fill-opacity', 0);
                });
});