# PrepareCD

Програма для підготовки технічної документації компресорів.

## Опис

PrepareCD спрощує роботу з маніпуляцією файлів під час підготовки технічної документації, що дозволяє прискорити роботу
і зосередитися безпосередньо на роботі з документами машини.

### Основний функціонал

- Перегляд файлів у віддалених і локальних каталогах для кожного замовлення
- Підготовка XLS файлів для подальшої роботи з CD
- Копіювання необхідних базових креслень
- Підготовка CD
- Парсинг спеціальних XLS файлів за допомогою Apache POI
- Робота в offline режимі для локальних файлів

### Архітектура

Інтерфейс побудований на JavaFX і виконаний у вигляді двопанельного файлового менеджера. Програма працює в парі зі
штатними файловими менеджерами системи (Total Commander, Double Commander, Explorer).

### Функції програми

Функціонал розділений на окремі модулі:

- **Підготовка креслення інсталяції** - автоматична підготовка установчих креслень
- **Підготовка XLS файла** - обробка та форматування даних замовлення
- **Підготовка креслень компресора** - копіювання та переіменування креслень
- **Підготовка CD** - автоматичне створення структури диску

## Використання

### Початок роботи

1. У текстове поле введіть назву машини
2. Натисніть `Enter`
3. Програма скопіює файл замовлення в локальний каталог, прочитає необхідні дані та відобразить їх в інтерфейсі
4. Файли будуть показані в локальному і віддаленому каталогах

### Робочий процес

1. Залежно від завдання натисніть відповідну кнопку функції
2. Слідуйте інструкціям у рядку статусу
3. За потреби виберіть додаткову конфігурацію для зміни коду машини
4. Код машини можна змінити вручну, натиснувши на нього

### Додаткові можливості

- **Скидання коду за замовчуванням**: очистіть поле зміни коду і натисніть `Enter`
- **Автоматична навігація**: при копіюванні базових файлів програма автоматично переходить у потрібний каталог
- **Ручна зміна каталогу**: можливість змінити каталог вручну при необхідності

## Offline режим

Програма підтримує роботу в offline режимі для випадків, коли відсутній доступ до віддалених каталогів.

### Особливості offline режиму

- Робота тільки з локальними файлами
- Відключення функцій, які потребують доступу до віддалених ресурсів
- Індикатор "*OFFLINE*" в заголовку програми
- Можливість вибору LUX файлів з локального каталогу
- Спрощений інтерфейс файлового менеджера

### Увімкнення offline режиму

Offline режим можна увімкнути/вимкнути через меню програми. При увімкненні режиму:

- Заголовок програми відображає статус "*OFFLINE*"
- Віддалена панель файлового менеджера стає неактивною
- Функції копіювання з віддалених ресурсів недоступні

## Технічні вимоги

- **Java**: 17+
- **JavaFX**: 17.0.6
- **Apache POI**: 5.2.3 (для роботи з Excel файлами)
- **Операційна система**: Windows, Linux, macOS

## Конфігурація

Програма зберігає дані про замовлення у properties файлі в локальному каталозі замовлення. Налаштування включають:

- Дані про машину
- Додаткові опції (subbase, industrialization)
- Шляхи до базових креслень
- Налаштування файлових менеджерів

## Підтримувані файлові менеджери

- Total Commander
- Double Commander
- Windows Explorer

## Збірка

Проект використовує Maven для збірки:

```bash
mvn clean package
```

Для запуску з JavaFX:

```bash
mvn clean javafx:run
```

## Версія

Поточна версія: 0.8.8.6

---

# PrepareCD

A program for preparing technical documentation for compressors.

## Description

PrepareCD simplifies file manipulation work during technical documentation preparation, allowing you to speed up work
and focus directly on working with machine documents.

### Main functionality

- View files in remote and local directories for each order
- Prepare XLS files for further CD work
- Copy necessary basic drawings
- Prepare CD
- Parse special XLS files using Apache POI
- Work in offline mode for local files

### Architecture

The interface is built on JavaFX and implemented as a dual-panel file manager. The program works in conjunction with
standard system file managers (Total Commander, Double Commander, Explorer).

### Program functions

Functionality is divided into separate modules:

- **Installation drawing preparation** - automatic preparation of installation drawings
- **XLS file preparation** - processing and formatting order data
- **Compressor drawing preparation** - copying and renaming drawings
- **CD preparation** - automatic disk structure creation

## Usage

### Getting started

1. Enter the machine name in the text field
2. Press `Enter`
3. The program will copy the order file to the local directory, read the necessary data and display it in the interface
4. Files will be shown in local and remote directories

### Workflow

1. Depending on the task, press the corresponding function button
2. Follow the instructions in the status bar
3. If needed, select additional configuration to change the machine code
4. The machine code can be changed manually by clicking on it

### Additional features

- **Reset code to default**: clear the code change field and press `Enter`
- **Automatic navigation**: when copying basic files, the program automatically navigates to the required directory
- **Manual directory change**: ability to change directory manually if necessary

## Offline mode

The program supports offline mode for cases when there is no access to remote directories.

### Offline mode features

- Work only with local files
- Disable functions that require access to remote resources
- "*OFFLINE*" indicator in the program title
- Ability to select LUX files from local directory
- Simplified file manager interface

### Enabling offline mode

Offline mode can be enabled/disabled through the program menu. When enabled:

- Program title displays "*OFFLINE*" status
- Remote file manager panel becomes inactive
- Functions for copying from remote resources are unavailable

## Technical requirements

- **Java**: 17+
- **JavaFX**: 17.0.6
- **Apache POI**: 5.2.3 (for working with Excel files)
- **Operating System**: Windows, Linux, macOS

## Configuration

The program stores order data in a properties file in the local order directory. Settings include:

- Machine data
- Additional options (subbase, industrialization)
- Paths to basic drawings
- File manager settings

## Supported file managers

- Total Commander
- Double Commander
- Windows Explorer

## Build

The project uses Maven for building:

```bash
mvn clean package
```

To run with JavaFX:

```bash
mvn clean javafx:run
```

## Version

Current version: 0.8.8.6