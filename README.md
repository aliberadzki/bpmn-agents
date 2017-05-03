# bpmn-agents

configure agents you want to run in `/src/main/resources/jade-agent-container.properties`

to run project  firstly run main container :
`mvn -Pjade-main exec:java`
and then agents themselves
`mvn -Pjade-agent exec:java`
