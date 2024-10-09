# Gen-AI-Powered-AR-App

Welcome to the **Gen-AI-Powered-AR-App** repository! This project explores various architectures for generating 3D images from 2D images and implements a text-to-image generation model. It also includes a Kotlin application that leverages the Meshy API to visualize 3D models in augmented reality using ARCore.

## Table of Contents

- [Project Overview](#project-overview)
- [Datasets](#datasets)
- [Notebooks](#notebooks)
- [Kotlin Application](#kotlin-application)
- [Installation](#installation)
- [Acknowledgments](#acknowledgments)
- [License](#license)

## Project Overview

This repository contains several Jupyter notebooks showcasing the following:

1. **3D Image Generation**: Implementation of various architectures for converting 2D images into 3D images using the **Pix3D dataset**.
2. **Text-to-Image Generation**: Techniques for generating images from text descriptions using the **CUB200-2011 dataset**.
3. **AR Visualization**: A Kotlin application that utilizes the **Meshy API** to render 3D models in augmented reality based on user prompts.

## Datasets

- **Pix3D Dataset**: This dataset is used for training models to generate 3D images from 2D images.
- **CUB200-2011 Dataset**: A dataset for text-to-image generation, containing images of birds with corresponding textual descriptions.

## Notebooks

The following notebooks are included in this repository:

1. `3D_Image_Generation_Pix3D.ipynb`: Various architectures for 3D image generation from 2D images.
2. `Text_to_Image_Generation_CUB200.ipynb`: Implementations of text-to-image generation methods.
3. `Pix3D_Architecture_Comparison.ipynb`: Comparison of different models for 3D generation.

## Kotlin Application

The Kotlin app provides an interface for users to input prompts, which are processed to visualize a 3D model using the Meshy API and ARCore. This application enhances user interaction by allowing them to see generated 3D models in an augmented reality environment.

### Key Features:

- User-friendly interface for inputting prompts.
- Real-time visualization of 3D models in AR.
### Running the Application
- Open the Kotlin project in your preferred IDE.
- Ensure the Meshy API is correctly set up and configured.
- Run the application and follow the instructions to visualize 3D models in AR.

## Installation

To get started with the project, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/Seif-Yasser-Ahmed/Gen-AI-Powered-AR-App.git
   ```
2. Navigate to the project directory:
    ```bash
    cd Gen-AI-Powered-AR-App
    ```

3. Install the required Python packages:
    ```bash
    pip install -r requirements.txt
    ```

## Acknowledgments
We would like to express our gratitude to the following repositories and their contributors for their valuable resources:

PiFuHD by MetaResearch for providing the framework and models used in this project.
ICML 2016 Text-to-Image Generation for inspiring methodologies in text-to-image generation.
Pix2Vox for its implementation and pretrained models, which contributed to our 3D image generation efforts.
Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue for any suggestions or improvements.

## License
This project is licensed under the MIT [`LISENCE/`](LICENSE). See the LICENSE file for more details.

Feel free to adjust any sections or content to better suit your needs!

