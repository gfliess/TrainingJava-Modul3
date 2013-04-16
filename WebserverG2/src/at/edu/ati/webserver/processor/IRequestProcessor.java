
/** 
 * Copyright 2013 SSI Schaefer PEEM GmbH. All Rights reserved. 
 * <br /> <br />
 * 
 * $Id$
 * <br /> <br />
 *
 */

package at.edu.ati.webserver.processor;

import java.nio.channels.Channel;


/**
 * This is the class header. The first sentence (ending with "."+SPACE) is important,
 * because it is used summary in the package overview pages.<br />
 * <br />
 *
 *
 * @author  jog
 * @version $Revision$
 */

public interface IRequestProcessor extends Runnable {

  void addData(String data, Channel channel);
  
  void setDirectory(String directory);
	
}


//---------------------------- Revision History ----------------------------
//$Log$
//
