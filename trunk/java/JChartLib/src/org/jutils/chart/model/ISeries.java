package org.jutils.chart.model;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface ISeries
{
    public ISeriesData getData();

    public IMarker getMarker();

    public IMarker getHighlighter();

    public ILine getLine();

    public String getName();
}
