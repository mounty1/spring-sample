set -e
REPORT='\n>>>> status=%{response_code} type=%{content_type}\n'
for C in a 1 2 b c d 2 3 4 e 1 f z
do
	curl -H Accept:application/json -H Content-Type:application/json -X POST -d '{"count":"1","content":"'${C}'"}' -w "${REPORT}" http://127.0.0.1:8080/chars
done
curl -H Accept:application/json -H Content-Type:application/json -X GET -w "${REPORT}" http://127.0.0.1:8080/state
test $(curl -H Accept:application/json -H Content-Type:application/json -X GET -s http://127.0.0.1:8080/sum | python -c "import sys, json; sys.stdout.write(json.load(sys.stdin)['content'])") == "247" || echo WRONG SUM
# test $(curl -H Accept:application/json -H Content-Type:application/json -X GET -s http://127.0.0.1:8080/huh | python -c "import sys, json; sys.stdout.write(json.load(sys.stdin)['status'])") -eq 404 || echo WRONG
curl -H Accept:application/json -H Content-Type:application/json -X DELETE -w "${REPORT}" http://127.0.0.1:8080/chars/3
test $(curl -H Accept:application/json -H Content-Type:application/json -X GET -s http://127.0.0.1:8080/sum | python -c "import sys, json; sys.stdout.write(json.load(sys.stdin)['content'])") == "37" || echo WRONG SUM
for C in a 1 2 b c d 2 4 e 1 f z
do
	curl -H Accept:application/json -H Content-Type:application/json -X DELETE -w "${REPORT}" http://127.0.0.1:8080/chars/${C}
done
test -z $(curl -H Accept:application/json -H Content-Type:application/json -X GET -s http://127.0.0.1:8080/state | tee ex | python -c "import sys, json; sys.stdout.write(json.load(sys.stdin)['content'])") || echo NOT CLEARED
