
100 parallel requests
`seq 100 | xargs -n1 -P100 -I{} curl -s "http://localhost:8080/hello"`

1 request per second
`while true; do   curl -s http://localhost:8080/hello;   echo ""                                 
sleep 1; done`