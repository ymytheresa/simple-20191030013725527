rm -rf generated*

swagger-codegen generate \
-i backend_swagger20.json \
-l java \
-o generated_java \
--api-package com.bochk.hackathon.api.functional.api \
--model-package com.bochk.hackathon.api.functional.model \
--type-mappings number=Double,integer=Long \
--additional-properties dateLibrary=java8 \
--library retrofit2

swagger-codegen generate \
-i frontend_swagger20.json \
-l java \
-o generated_java \
--api-package com.bochk.hackathon.api.oauth.api \
--model-package com.bochk.hackathon.api.oauth.model \
--type-mappings number=Double,integer=Long \
--additional-properties dateLibrary=java8 \
--library retrofit2

rsync -r --delete --progress generated_java/src/main/java/com/bochk/hackathon/api ../src/main/java/com/bochk/hackathon

find ../src/main/java/com/bochk/hackathon/api -type f -name "*.java" -exec gsed -i '/javax.annotation.Generated/ d' {} \;

