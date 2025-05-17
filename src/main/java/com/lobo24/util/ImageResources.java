package com.lobo24.util;

import javafx.scene.image.Image;

public class ImageResources {
    private static final String IMAGES_PATH = "/com/lobo24/images/";

    // Logo de la aplicación
    public static final Image LOGO_APP = new Image(
            ImageResources.class.getResourceAsStream(IMAGES_PATH + "logo_lobo24.png"));

    // Íconos varios
  // public static final Image ICON_USER = new Image(
         //   ImageResources.class.getResourceAsStream(IMAGES_PATH + "icon_user.png"));
  //  public static final Image ICON_PASSWORD = new Image(
        //    ImageResources.class.getResourceAsStream(IMAGES_PATH + "icon_password.png"));
  //  public static final Image ICON_INFO = new Image(
        //    ImageResources.class.getResourceAsStream(IMAGES_PATH + "icon_info.png"));

    // Método para cargar imágenes dinámicamente
    public static Image loadImage(String imageName) {
        try {
            return new Image(ImageResources.class.getResourceAsStream(IMAGES_PATH + imageName));
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen: " + imageName);
            return null;
        }
    }
}