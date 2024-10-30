#
# /*******************************************************
#  * Copyright (c) 2024 htilssu
#  *
#  * This code is the property of htilssu. All rights reserved.
#  * Redistribution or reproduction of any part of this code
#  * in any form, with or without modification, is strictly
#  * prohibited without prior written permission from the author.
#  *
#  * Author: htilssu
#  * Created: 30-10-2024
#  *******************************************************/
#

sudo docker pull htilssu/ewallet-springboot:staging
#set up the environment variables


sudo docker run -d -p 8081:8080 htilssu/ewallet-springboot:staging
sudo docker ps
sudo docker image prune -f