package fr.insa.beuvron.vaadin.projets.tournoi.webui.utils;

import java.awt.image.BufferedImage;
import java.nio.Buffer;

import com.vaadin.flow.component.html.Image;

public class SmallImage {

    public static final String PNG_TYPE = "image/png";

    public static final SmallImage PETIT_SMILEY_CONTENT_PNG = new SmallImage(
            "iVBORw0KGgoAAAANSUhEUgAAADIAAABLCAYAAAAyEtS4AAAAAXNSR0IB2cksfwAAAARnQU1BAACxjwv8"
                    + "YQUAAAAgY0hSTQAAeiYAAICEAAD6AAAAgOgAAHUwAADqYAAAOpgAABdwnLpRPAAAAAZiS0dEAP8A/wD/"
                    + "oL2nkwAAAAlwSFlzAAAuIwAALiMBeKU/dgAAAAd0SU1FB+kLGwcfA1xLSMQAAAhHSURBVGje7ZrbjhxX"
                    + "FYa/vauqT9WeGY8nbp+wjZNJYhJIHIcIiAj4AqQAAiEkeAXueQseIK+AIJCrXEQQIkIgwVEUHMA22Els"
                    + "YrCn7cl43NM9fajae3NRq3BNTx+q2+0oinpJWz1qddXUv/91+NfaBXOb29zmNre5ze2zY2rWN2zWw133"
                    + "rNZa7lMLpFkPNZAuT1YA+LIU4AADxEBPVgy4WYNTUwDwgQqwIKsKhLIqQAkoCkAnD98CGsBtYAPYlO96"
                    + "1VrLfKJAxGUqwAHguKzDwD4BFAJlARJkgERAG7gDrAM3gP/IuiHfNYHoXlhSE4BYAB4ETslaFVB7hIHU"
                    + "rTwBkbpW6l49AdSQh78OfARcAT6Uv28KU2ZSUH7O31UExNeB54DHgf3CgCcPrcZsTArKCksdYEtAXQP+"
                    + "CbwH/B241qyHzWqtZWfGiMTEMeAM8B3gaQGRus+0loIyAmoD+AA4C/xBQH2cN4byMBIKG6czTBRmkLpV"
                    + "X7YLgRWJuwOSRN5s1sNbeZjxc6TYZeBh4NEME+o+lAFPwJwQlw0kVf+pWQ83x8XMOEYC2Z2HZKfKA9wp"
                    + "9X01gyKrhO1DEo9tiaO3ge1RF+ocQX4YOAosDQCe+vmslUIAHBQw3wJOSKxODkTcaiHjs5UhD5onY01j"
                    + "BdnAbwBfBfYNkj95GPEkPg7J56DYUJmaMW3mGuVmZalXzwGPyDNMDKQgAGrCjDfjHXeZNer5loAvSdpf"
                    + "ngZIUeTHiriVvg+ZSuVgJhD3fmpUrOgRkqQE7JU1um5YlSw3NRg15jdVca0viHfkTr9pGtwja/DvjMbc"
                    + "KGM+LIHn8I70UGUDJYsOI/BG1DEnwJ0C5UC7PGXgJLC/WQ/v9Fd8f8zFoTAzcMfsRpHeLxcxr/hQdOjT"
                    + "5WQXFhzesx2CJxuoUrwrMmyjiLlSxt3ycQZU1eI92MGrtYcBUrKhqeL+t9SYXIz4Eif+UCDrBexZL9Gs"
                    + "JYV7T+OuJTttL1fQP4vwH9racbVrFui9vIR5qYC7JawsOfyfBOjvx6iF3rDnKYqyOAiUm/Wwk632OqcW"
                    + "GgjExTjXu3snt6kSCWiADQW93bc39SLmtwXc+0q6EKClcG2FcyOzuCfxsSzJZyKJMtJx9R6DOuhwV1Ui"
                    + "INqJQ6pjDu8HPfSRzu4t6OhEdChQR11SKbZAVRzKt+MSQzHT/6js840CYkW0Db27rnUIftwmrpVgU0HF"
                    + "oU8YvFNd/IdbqOoANynaJPIcuHWVMGKBtkoCfzQQLUloV3H2RzCRdnTxMGZUOSZ4dtP5TwTKRRrlW1TF"
                    + "JAGuBpOpVyL0EwZz0U+Y0aBOW7ynuqhylMc7vEEhMQpIV9rO7ggXU6pgnFrOPz/Qi12C51u47RB3yUMd"
                    + "tfjfa+M/1gBvbCFyw+rOQCDVWss162FHhgJdpil1Q5E4/Ee30D/tYRs+qmrQ+7qowOQBYafpR3oCpJND"
                    + "3E1mnkXvb6P3T63PJtJasTQznVE78QladnjhJgHiJNhbAurTACTNpGYaII2hmcspMHpc2ryX3e//LsqO"
                    + "XScBsi1jzs4gxWvWykTnFjFr5UT93l8QVhJPWwDlBmLFrTYE0I4L3XZA9Ooeej+vEr2ygGsFs3SfQQFt"
                    + "BERTWMntWkYu2kjHmDv+Y09jr/i4Swrzho/5b+neknSscduBI9bDakU6yEuH3/kYkR9uC5BGPxBVMuhD"
                    + "BgJwFzXR70PsenkqMG47oPf2kuu+tKyiCwueizw1RjZNFOyIT96S+WzUL0+8Zzqokw7aYH4T0H1xCXM9"
                    + "nCheXMcnOrtA9EKo4heKyrxVUnQGAvGy0/7+ico49RvLhPyaDMrC/4NXDn+1if1hgehWEa4rzC8CumtL"
                    + "+GdKeI900MtdVGCTZqlfe8Uac7NMfDYk/nURd0HBXlA1AwUzqmNdFBl/O8vMOCBG2HgfqMsworhDNJ65"
                    + "A2qB+Fcl3AWFfdmj91aI/mIZfTJGHTboB2LU3hhVsWDBNXzM5QLmjQL2HS859imD990I/ystVNEwgpFK"
                    + "RsbnZsSSHNB8QHKOcbx/EKEXuxS+fRu1skD8YgX7roZ1sK9p7B8LiSMsONRy8pk2XW5NJRwDPADe8xGF"
                    + "H20l7e7wjGaGZbaRQEQ8tkkOZT4Anhw0jFDlmOCZO3iHe8QXyphzAe68h7shDVdd4a73yTI/mZjpUwbv"
                    + "m12Cp1vofZ2h8l9S7u1MObCTHisY4GNp+OvSM++6TgUG73gT73Pb2K8F2HoRuxbg1j3sTY276SW5j6RH"
                    + "14ctejXCX22jVzowujs0JA54RZ5jqz/95gFipZ58BFyVs5Li0IznWfRSF73UhdVk3uUijet6d3v4okWV"
                    + "TCLdda4epAlcBt4Rz2hP2rOn7rVNcnh5Xo4YwhFD7R29BzrpxVV5Kt3ppABeBl4H/gKsDTrFynuG2JU4"
                    + "+RtwROLk2KiZ1wws9YR/Ab8DXhU2upM2VllWbLMebggjVclcVsb+92MuHEtMXARek3V+UGxM1d0162FR"
                    + "Jn2nZdT/ZeDzMjGfxZGcFT11U9h/HfizsNIYdZY4zZsPgUz8Hhcgp0nOGGsSO8EUZyZW0uumJJR3gTeB"
                    + "v0qSaY07Q5xqB5v1MJ36HQceE1CrwtaKxFBFXDA7cs0O1VyGgYYkkwsC4pzEw3q11urlHeszJZhU/+yT"
                    + "BHBUPg9kVk2OJSrsfK0jleSbkkQuAf8QIFdFFrVn+sJADkBa6krI3RdtlsT9DkkBXZEkEQiItD24LkXu"
                    + "qvy9CXQmATAzIH0Mpa89+Rlwi+JqJRF+qTs1Rcc10vb1vr9Uc49sZVc2uC1gp9n9uc1tbnOb29zmNre5"
                    + "zW1unwn7H5LvLQOS5lshAAAAAElFTkSuQmCC",
            SmallImage.PNG_TYPE);

    public static final SmallImage PETIT_SMILEY_PAS_CONTENT_PNG = new SmallImage(
            "iVBORw0KGgoAAAANSUhEUgAAADIAAABLCAYAAAAyEtS4AAAAAXNSR0IB2cksfwAAAARnQU1BAACxjwv8" +
                    "YQUAAAAgY0hSTQAAeiYAAICEAAD6AAAAgOgAAHUwAADqYAAAOpgAABdwnLpRPAAAAAZiS0dEAP8A/wD/" +
                    "oL2nkwAAAAlwSFlzAAAuIwAALiMBeKU/dgAAAAd0SU1FB+kMCgodLnn/CYoAAAhISURBVGje7ZrrjxtX" +
                    "GcZ/58zF1+xuNkucTVdJu9uEQkNvC5VC1FYIIdQQioRUkPgL+Ir4f/jeD5QP8AGlULUCSlMq2kJDSZq0" +
                    "uaCmbtKN47U9vsycw4d5h8zu2uOx41So8isdrbXyjOc5z/Nez8Dc5ja3uc1tbnP78pia9Q1b9cqee1Zr" +
                    "bft/C6RVr2ggWY4sD3BlKcACERACfVkhYGcNTk0BwAXKwIKsKlCRVQaKQEEAWnn4NtAEbgNbQEP+16/W" +
                    "2tEXCkQkUwYOAQ/KegA4IIAqQEmAeCkgAyAA7gC3gBvAf2TdkP+1gMG9sKQmALEAbABPyjomoPYJA4ms" +
                    "HAGRSCuRV18ANeXhPwGuAR8DH8nnz4SpaFJQbs7vlQXEM8CzwAngoDDgyEOrMRuTgDLCUhfYFlDXgX8D" +
                    "7wH/BK636pVWtdY2M2NEfOIo8B3gNPBNAZHIZ1pLQEUCagu4DJwDXhNQn+f1oTyMVISNzRQT/gxCt9oV" +
                    "7SrAivjdIQkib7TqlZt5mHFzhNhl4DjwSIoJdR/SgCNg1kWynoTqP7fqlcY4nxnHiCe787DsVGmInBLt" +
                    "qxkkWSVsHxZ/DMSP3gI6WRfqHE7+AHAEWBoCPNH5rCsFD1gVMN8D1sVXJwcislpIabY84kHzRKxpzJcN" +
                    "fA44CRwYVv7kYcQR/zgsf4f5hkrljGkjV5bMSpKvngW+Ks8wMRBfANSEGWfGO25TK+v5loDHJOwvTwOk" +
                    "IOXHishK34dIpXIw44m8n8ryFZ1RkhSB/bKy84ZR8bJTg1FjvlMVaX1d1JE7/CZhcJ+s4d+LNNGNEtFH" +
                    "RXAszlofVYqgaNCVATgZecwKcKtAWdA2Txr4GnCwVa/c2Z3x3TEXV4SZoTtmtgr0X1ok+r0LBYveLMW7" +
                    "sGBxTnXxnmiiiuEezzDNAtHHJexNFxuBqhqcjS5OLRgFSMmGJhX3VckxuRhxxU/ckUBu+ZhzTlyzFhX2" +
                    "PY29Hu+0+bCM/uUA9+HtHVfblk//d0tEL/vYm8LKksX9qYd+IUQt9Ec9T0Eqi1Wg1KpXuulsr3PWQkOB" +
                    "2BBr+3fvZBsqLgEjYEtBf+/to3qB6KyPvaSkCwHaChsorM2M4o74x7IEn4lKlEzh6n0RatVir6i4gAhi" +
                    "QaqjFudHffRad+8WdHVcdChQR2ycKbZBlS3KNeMCQyHV/6j082UBMVK0jby7rnXxfhIQ1orQUFC26PUI" +
                    "58ke7vE2qjpEJgUTe54Fe0vFjBggULHjZwPREoT2JGc3g4mkowtHMaNKId6phnUf95QdaJRrUOUodnA1" +
                    "nEy9MkA/HhF94MbMaFCbBuepHqo0yKMOZ5hLZAHpSdvZy5CYUn5k1XL++YFe7OE938Z2KtiLDuqIwT0T" +
                    "4D7aBGdsIrKj8s5QINVa27bqla4MBXpMk+pGIrG4j2yjf97HNF1UNUIf6KG8KA8IM00/0hcg3RzF3WTm" +
                    "GPTBAH1w6vpsolorlGamm7UTX6Clhxd2EkasOHtbQOW3UGPaHvQ0FoXSFlUJM4PABMOKUDLVxECaqciV" +
                    "LSOrMLcLhO9UCf/mwy0pJgsWfTzCPdnBWW+j/Fz+sFu2ybCvPyySjgPSkTFnF1jM/OWeQ3S5yuBsmeis" +
                    "Czd2VsPmVYfonIv3oo938g6qPBgnITWEjZ5s7mASIEZktSWARjJiOx79Py0S/rqIfVfHe3YA1LqJS85P" +
                    "FfaKwr6pGTRK4Fr8bzdgb6RKR6bdvxUJiJawkltakVy0lYwxhwaHgWbw9gLhr0rYCwp8UKcM7uke7okA" +
                    "fIOp+4Rny0S/dbEXFIOXSujlMM4dO6tdI8sZAaSbGn7nY0RySUeANOVG3m6fCK9WCF8uYS8qWADnhRDv" +
                    "hy2ch+76gnMoQH9lQC9axPzGxb6l6R+sovaHOGvtNAiyitQsZx/XvvaAmzKfHQzzi/CvZcw5DS7oH4T4" +
                    "P2vgHt/e6dDK4qx28H/cQj8TgQHzR4fBa1Vsx0s7t84YZjjpaf/uico4IKFMyK/LoGxHPrFND3PehQDU" +
                    "hsX7fgdnNRgeYrXF3WjhvRigjlnYhugVn+hayQqMrJY33bEuDpshjAMSCRuXgPoeVqyCkoVl0M+FuOtB" +
                    "dp5wDe43ttHfHYAP9pIier+o7MDJMxdLGCmnyvjcQIwc0FyWc4xOOuypxT7emQDvFwH+6e3hZfvurS2H" +
                    "uE93URsWAjDXHGzXzZsQo1HZPfMO4vCBHMpcBp5IDyNUMcR9rAknVFz06RxZW1nc9Q7RmQLRkofeCMc1" +
                    "VOna73YqHZhJjxUi4HNp+OvSM//vuhxV614s+wb4z9/BPO2jV/qoUpjnGRqiiqvAdu7wu0teLeKjsSty" +
                    "VlK4p4GdsujlLnp/L0/tZeX3PwTeFmUEk4bf5Iy8Q3x4eT41irn3HiUfiLaAeB14E/h02ClW3jPEnvjJ" +
                    "P4A18ZOjWTOvGViihAvAK8AfhI3epI1VmhXTqle2hJGqxHQjY//7MRcOxSc+AF6VdX6Yb0zV3bXqlYJM" +
                    "+jZl1P8t4CGZmM/iSM5IPfWZsP868BdhpZl1ljjNmw+eTPxOCJBN4jPGmoxYvSnOTIyE14YElL8DbwDv" +
                    "SJBpjztDnGoHW/VKMvV7EHhUQB0TtlbEh8oiwfTINT1UsykGmhJM/iUg3hV/uFWttfu54sa0GpCizZcz" +
                    "lDXxlzWZmierJscSZXa+1pGU5A0JIheB9wXIFSmLgpm+MJADkJa8UuHuizZLIr/DkkBXJEh4AiJpDz6R" +
                    "JHdFPjeA7iQAZgZkF0NJGe6mwC2K1IpS+CVyakkd10za1/v+Us09spVeuztBM83uz21uc5vb3OY2t7nN" +
                    "bW5z+1LYfwFIDiZLvR/6jAAAAABJRU5ErkJggg==",
            SmallImage.PNG_TYPE);

    private byte[] imageData;
    private String imageType;

    public SmallImage(byte[] imageData, String imageType) {
        this.imageData = imageData;
        this.imageType = imageType;
    }

    public SmallImage(String base64, String imageType) {
        this.imageData = java.util.Base64.getDecoder().decode(base64);
        this.imageType = imageType;
    }

    public String toBase64() {
        return java.util.Base64.getEncoder().encodeToString(this.imageData);
    }

    public String getImageDataURI() {
        return "data:" + this.imageType + ";base64," + this.toBase64();
    }

    public Image toVaadinImage(String altText) {
        String dataURI = this.getImageDataURI();
        Image img = new Image(dataURI, altText);
        img.setWidthFull();
        img.setHeightFull();
        img.getStyle().set("object-fit", "contain");
        return img;
    }

    public BufferedImage toBufferedImage() throws Exception {
        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(this.imageData);
        return javax.imageio.ImageIO.read(bais);
    }

    public static SmallImage fromBufferedImage(BufferedImage img, String imageType) throws Exception {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        javax.imageio.ImageIO.write(img, imageType.split("/")[1], baos);
        byte[] imgData = baos.toByteArray();
        return new SmallImage(imgData, imageType);
    }

    public SmallImage resizeToPNG(int maxWidth, int maxHeight) throws Exception {
        BufferedImage original = this.toBufferedImage();
        BufferedImage resized = ImageResizer.resizeKeepAspectRatio(original, maxWidth, maxHeight);
        return SmallImage.fromBufferedImage(resized, SmallImage.PNG_TYPE);
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getImageType() {
        return imageType;
    }

}
