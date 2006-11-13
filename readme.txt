- Install MySQL (flush grant tables after inserting new users)
- Modify the data-beans.xml file in: /Rhythmyx/AppServer/server/rx/deploy/RxServices.war/WEB-INF/config
To point to the correct MySQL server
- Run ant: ant -f deploy.xml