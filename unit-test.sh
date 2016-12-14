 set -e
 REPORT='\n>>>> status=%{response_code} type=%{content_type}\n'
 for C in a 1 2 b c d 2 3 4 e 1 f z
 do
	curl -H Accept:application/json -H Content-Type:application/json -X POST -d '{"count":"1","content":"'${C}'"}' -w "${REPORT}" http://127.0.0.1:8080/chars
done
curl -H Accept:application/json -H Content-Type:application/json -X GET -w "${REPORT}" http://127.0.0.1:8080/state
curl -H Accept:application/json -H Content-Type:application/json -X GET -w "${REPORT}" http://127.0.0.1:8080/sum
curl -H Accept:application/json -H Content-Type:application/json -X GET -w "${REPORT}" http://127.0.0.1:8080/huh
curl -H Accept:application/json -H Content-Type:application/json -X DELETE -w "${REPORT}" http://127.0.0.1:8080/chars/3
curl -H Accept:application/json -H Content-Type:application/json -X GET -w "${REPORT}" http://127.0.0.1:8080/sum
